package yanyu.xmz.recorder.handler;

import com.github.shyiko.mysql.binlog.event.*;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yanyu.xmz.recorder.dao.BaseDAO;
import yanyu.xmz.recorder.entity.EventRecord;
import yanyu.xmz.recorder.enums.StateEnum;
import yanyu.xmz.recorder.enums.StepEnum;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 */
public abstract class AbstractMysqlEventHandler implements DbEventHandler {

    protected static final Gson gson = new Gson();

    private static Long binLogStartPos = null;

    private static final Logger log = LoggerFactory.getLogger(AbstractMysqlEventHandler.class);

    @Override
    public void saveEvent(Long startPos, Event event) {

        if(binLogStartPos == null) {
            binLogStartPos = startPos;
        }

        EventRecord eventRecord = initEventRecord(event);

        try {
            saveEventDetailToDatabase(event, eventRecord);
            eventRecord.setState(StateEnum.SUCCESS.name());
        } catch (Exception e) {
            log.error("保存事件信息失败,startPos={}", startPos, e);
            eventRecord.setState(StateEnum.FAIL.name());
        }

        recordNextBinLogPos(event, eventRecord);
    }


    private EventRecord initEventRecord(Event event) {
        EventType eventType = event.getHeader().getEventType();
        EventRecord dataRecord = new EventRecord();
        dataRecord.setOperationType(eventType.name());
        dataRecord.setPos(binLogStartPos);
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
     * @see com.github.shyiko.mysql.binlog.BinaryLogClient 类
     * 方法 updateClientBinlogFilenameAndPosition
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
    }


}
