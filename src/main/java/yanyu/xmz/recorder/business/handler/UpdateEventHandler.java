package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yanyu.xmz.recorder.business.entity.event.EventRecord;
import yanyu.xmz.recorder.business.entity.event.UpdateRowRecord;
import yanyu.xmz.recorder.business.entity.yanysql.TUpdateRowRecord;
import yanyu.xmz.recorder.business.enums.StepEnum;

import java.io.Serializable;
import java.util.*;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 */
public class UpdateEventHandler extends AbstractMysqlEventHandler {

    private static final Logger log = LoggerFactory.getLogger(UpdateEventHandler.class);

    @Override
    protected EventRecord saveEventDetailToDatabase(Event event, EventRecord eventRecord) {

        // 构造事件信息对象
        UpdateRowsEventData updateRowsEventData = event.getData();
        eventRecord.setTableId(String.valueOf(updateRowsEventData.getTableId()));
        eventRecord.setIncludedColumns(updateRowsEventData.getIncludedColumns().toString());
        eventRecord.setIncludedColumnsBeforeUpdate(updateRowsEventData.getIncludedColumnsBeforeUpdate().toString());
        eventRecord.setStep(StepEnum.SAVE_EVENT.name());
        eventRecord.setTableName(tableName);
        eventRecord.setDatabaseName(databaseName);
        eventRecord.setIncludedColumnNames(getColumnNames(updateRowsEventData.getIncludedColumns()));
        eventRecord.setColumnNamesBeforeUpdate(getColumnNames(updateRowsEventData.getIncludedColumnsBeforeUpdate()));

        tableName = null;
        databaseName = null;
        // 事件信息保存到数据库
        baseExpDao.updateById(eventRecord, tableSuffix);

        // 保存更新事件变更记录
        List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
        List<UpdateRowRecord> updateRowRecords = new ArrayList<>(rows.size());
        for (Map.Entry<Serializable[], Serializable[]> row : rows) {
            updateRowRecords.add(new UpdateRowRecord(eventRecord.getId(), gson.toJson(row.getKey()), gson.toJson(row.getValue())));
        }
        baseExpDao.batchInsert(updateRowRecords, tableSuffix);

        // 保存到自己写的数据库
        try {
            insertYanysql(event);
        } catch (Exception e) {
            log.error("保存到自己写的数据库发生异常", e);
        }

        return eventRecord;
    }


    private void insertYanysql(Event event) {
        UpdateRowsEventData updateRowsEventData = event.getData();
        currRecord.setTableId(String.valueOf(updateRowsEventData.getTableId()));
        currRecord.setIncludedColumns(updateRowsEventData.getIncludedColumns().toString());
        currRecord.setIncludedColumnsBeforeUpdate(updateRowsEventData.getIncludedColumnsBeforeUpdate().toString());
        currRecord.setStep(StepEnum.SAVE_EVENT.name());
        currRecord.setTableName(tableName);
        currRecord.setDatabaseName(databaseName);
        currRecord.setIncludedColumnNames(getColumnNames(updateRowsEventData.getIncludedColumns()));
        currRecord.setColumnNamesBeforeUpdate(getColumnNames(updateRowsEventData.getIncludedColumnsBeforeUpdate()));
        currRecord.setUpdateTime(new Date());
        // 事件信息保存到数据库
        yanySqlBaseDAO.updateById(currRecord);

        // 保存更新事件变更记录
        List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
        for (Map.Entry<Serializable[], Serializable[]> row : rows) {
            TUpdateRowRecord tUpdateRowRecord = new TUpdateRowRecord(currRecord.getId(), gson.toJson(row.getKey()), gson.toJson(row.getValue()));
            tUpdateRowRecord.setId(UUID.randomUUID().toString());
            tUpdateRowRecord.setCreateTime(new Date());
            tUpdateRowRecord.setTenantId(tableSuffix);
            yanySqlBaseDAO.insert(tUpdateRowRecord);
        }
    }

}
