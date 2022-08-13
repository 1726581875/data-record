package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.*;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.entity.EventRecord;
import yanyu.xmz.recorder.business.entity.metadata.MysqlMetadata;
import yanyu.xmz.recorder.business.enums.StateEnum;
import yanyu.xmz.recorder.business.enums.StepEnum;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 */
public abstract class AbstractMysqlEventHandler implements DbEventHandler {

    protected static final Gson gson = new Gson();

    private static Long binLogStartPos;

    private static String binLogFileName;

    protected static String tableName;

    protected static String databaseName;

    /**
     * 元数据缓存
     * Map<dbName,Map<tableName,List<MysqlMetadata>>>
     */
    protected static Map<String, Map<String, List<MysqlMetadata>>>  metadataCacheMap = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(AbstractMysqlEventHandler.class);

    @Override
    public void saveEvent(Long startPos, String fileName, Event event) {

        if(binLogStartPos == null) {
            binLogStartPos = startPos;
            initMetadataCache();
        }

        if(binLogFileName == null) {
            binLogFileName = fileName;
        }


        log.info("=====>保存事件信息,类型={},pos={}",event.getHeader().getEventType().name(), binLogStartPos);

        EventRecord eventRecord = initEventRecord(event);

        try {
            // 保存事件信息和数据更改记录到数据库
            saveEventDetailToDatabase(event, eventRecord);

            eventRecord.setStep(StepEnum.SAVE_SUCCESS.name());
            eventRecord.setState(StateEnum.SUCCESS.name());
        } catch (Exception e) {
            log.error("保存事件信息失败,startPos={}", startPos, e);
            eventRecord.setState(StateEnum.FAIL.name());
        }

        recordNextBinLogPos(event, eventRecord);
    }


    protected void initMetadataCache() {
        synchronized (AbstractMysqlEventHandler.class) {

            List<MysqlMetadata> metadataList = BaseDAO.mysqlInstance().getList("select * from mysql_metadata", MysqlMetadata.class);
            if(metadataList == null || metadataList.size() == 0){
                return;
            }
            Map<String, Map<String, List<MysqlMetadata>>>  metadataMap = new HashMap<>();

            Map<String, List<MysqlMetadata>> map = metadataList.stream().collect(Collectors.groupingBy(MysqlMetadata::getTableSchema));
            map.forEach((k,v) -> metadataMap.put(k, v.stream().collect(Collectors.groupingBy(MysqlMetadata::getTableName))));

            // 列字段排序
            metadataMap.forEach((dbName, tableMap) -> {
                tableMap.forEach((tableName, columnList) -> {
                    columnList.sort(Comparator.comparingLong(MysqlMetadata::getOrdinalPosition));
                });
            });

            metadataCacheMap = metadataMap;

        }
    }


    private EventRecord initEventRecord(Event event) {
        EventType eventType = event.getHeader().getEventType();
        EventRecord dataRecord = new EventRecord();
        dataRecord.setEventType(eventType.name());
        dataRecord.setPos(binLogStartPos);
        dataRecord.setBinLogFileName(binLogFileName);
        dataRecord.setEventTimestamp(new Date(event.getHeader().getTimestamp()));
        dataRecord.setState(StateEnum.RUNNING.name());
        dataRecord.setStep(StepEnum.INIT.name());
        // 保存到数据库
        Long id = BaseDAO.mysqlInstance().insertReturnKey(dataRecord);
        dataRecord.setId(id);

        return dataRecord;
    }

    /**
     * 保存binlog事件详情到数据库
     *
     * @param event
     * @param eventRecord
     * @return
     */
    protected abstract EventRecord saveEventDetailToDatabase(Event event, EventRecord eventRecord);


    /**
     * 记录下一位置的binlog
     * 获取binlog下一位置，参考以下方法
     * @see com.github.shyiko.mysql.binlog.BinaryLogClient#updateClientBinlogFilenameAndPosition
     *
     *
     * @param event
     * @param dataRecord
     */
    private void recordNextBinLogPos(Event event, EventRecord dataRecord) {
        EventHeader eventHeader = event.getHeader();
        EventType eventType = eventHeader.getEventType();
        if (eventType == EventType.ROTATE) {
            RotateEventData rotateEventData = (RotateEventData) EventDeserializer.EventDataWrapper.internal(event.getData());
            dataRecord.setBinLogFileName(rotateEventData.getBinlogFilename());
            dataRecord.setEndLogPos(rotateEventData.getBinlogPosition());
            binLogStartPos = rotateEventData.getBinlogPosition();
            binLogFileName = rotateEventData.getBinlogFilename();
        } else
            // do not update binlogPosition on TABLE_MAP so that in case of reconnect (using a different instance of
            // client) table mapping cache could be reconstructed before hitting row mutation event
            if (eventHeader instanceof EventHeaderV4) {
                EventHeaderV4 trackableEventHeader = (EventHeaderV4) eventHeader;
                long nextBinlogPosition = trackableEventHeader.getNextPosition();
                if (nextBinlogPosition > 0) {
                    dataRecord.setEndLogPos(nextBinlogPosition);
                    binLogStartPos = nextBinlogPosition;
                }
            }

        BaseDAO.mysqlInstance().updateById(dataRecord);


        log.info("=====>保存事件信息成功,类型={},endPos={}",event.getHeader().getEventType().name(), binLogStartPos);
    }



    protected List<MysqlMetadata> getColumnList(String dbName, String tableName) {

        Map<String, List<MysqlMetadata>> map = metadataCacheMap.get(dbName);
        if(map == null) {
            return new ArrayList<>();
        }
        List<MysqlMetadata> metadataList = map.get(tableName);
        if(metadataList == null){
            return new ArrayList<>();
        }
        return metadataList;
    }


    protected String getColumnNames(BitSet columns) {
        List<MysqlMetadata> columnList = getColumnList(databaseName, tableName);
        if(columnList != null && columnList.size() > 0) {
            String[] columnNames = new String[columns.length()];
            columns.stream().forEach(i -> {
                if(i >= columnList.size()){
                    columnNames[i] = null;
                } else {
                    MysqlMetadata column = columnList.get(i);
                    if(column != null && column.getOrdinalPosition() == i + 1) {
                        columnNames[i] =  column.getColumnName();
                    } else {
                        columnNames[i] = null;
                    }
                }
            });

            return gson.toJson(columnNames);
        }
        return null;
    }


}
