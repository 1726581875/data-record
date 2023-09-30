package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yanyu.xmz.recorder.business.entity.event.EventRecord;
import yanyu.xmz.recorder.business.entity.event.InsertRowRecord;
import yanyu.xmz.recorder.business.entity.yanysql.TInsertRowRecord;
import yanyu.xmz.recorder.business.enums.StepEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 */
public class InsertEventHandler extends AbstractMysqlEventHandler {

    private static final Logger log = LoggerFactory.getLogger(InsertEventHandler.class);

    @Override
    protected EventRecord saveEventDetailToDatabase(Event event, EventRecord eventRecord) {
        // 构造事件
        WriteRowsEventData writeRowsEventData = event.getData();
        eventRecord.setTableId(String.valueOf(writeRowsEventData.getTableId()));
        eventRecord.setIncludedColumns(writeRowsEventData.getIncludedColumns().toString());
        eventRecord.setStep(StepEnum.SAVE_EVENT.name());
        eventRecord.setTableName(tableName);
        eventRecord.setDatabaseName(databaseName);
        eventRecord.setIncludedColumnNames(getColumnNames(writeRowsEventData.getIncludedColumns()));
        tableName = null;
        databaseName = null;

        // 保存事件详情信息
        baseExpDao.updateById(eventRecord, tableSuffix);

        // 保存插入事件数据变更记录
        List<Serializable[]> rows = writeRowsEventData.getRows();
        List<InsertRowRecord> insertRowRecords = new ArrayList<>(rows.size());
        for (Serializable[] row : rows) {
            insertRowRecords.add(new InsertRowRecord(eventRecord.getId(), gson.toJson(row)));
        }
        baseExpDao.batchInsert(insertRowRecords, tableSuffix);

        // 保存到自己写的数据库
        try {
            insertYanysql(writeRowsEventData);
        } catch (Exception e) {
            log.error("保存到自己写的数据库发生异常", e);
        }
        return eventRecord;
    }


    /**
     * 保存到自己写的数据库
     * @param writeRowsEventData
     */
    void insertYanysql(WriteRowsEventData writeRowsEventData) {
        currRecord.setTableId(String.valueOf(writeRowsEventData.getTableId()));
        currRecord.setIncludedColumns(writeRowsEventData.getIncludedColumns().toString());
        currRecord.setStep(StepEnum.SAVE_EVENT.name());
        currRecord.setTableName(tableName);
        currRecord.setDatabaseName(databaseName);
        currRecord.setIncludedColumnNames(getColumnNames(writeRowsEventData.getIncludedColumns()));
        currRecord.setUpdateTime(new Date());
        // 保存事件详情信息
        yanySqlBaseDAO.updateById(currRecord);

        // 保存插入事件数据变更记录
        List<Serializable[]> rows = writeRowsEventData.getRows();
        for (Serializable[] row : rows) {
            TInsertRowRecord tInsertRowRecord = new TInsertRowRecord(currRecord.getId(), gson.toJson(row));
            tInsertRowRecord.setCreateTime(new Date());
            tInsertRowRecord.setTenantId(tableSuffix);
            tInsertRowRecord.setId(UUID.randomUUID().toString());
            yanySqlBaseDAO.insert(tInsertRowRecord);
        }

    }
}
