package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yanyu.xmz.recorder.business.entity.event.EventRecord;
import yanyu.xmz.recorder.business.entity.event.DeleteRowRecord;
import yanyu.xmz.recorder.business.entity.yanysql.TDeleteRowRecord;
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
public class DeleteEventHandler extends AbstractMysqlEventHandler {

    private static final Logger log = LoggerFactory.getLogger(DeleteEventHandler.class);


    @Override
    protected EventRecord saveEventDetailToDatabase(Event event, EventRecord eventRecord) {

        DeleteRowsEventData deleteRowsEventData = event.getData();
        eventRecord.setTableId(String.valueOf(deleteRowsEventData.getTableId()));
        eventRecord.setIncludedColumns(deleteRowsEventData.getIncludedColumns().toString());
        eventRecord.setStep(StepEnum.SAVE_EVENT.name());
        eventRecord.setTableName(tableName);
        eventRecord.setDatabaseName(databaseName);
        eventRecord.setIncludedColumnNames(getColumnNames(deleteRowsEventData.getIncludedColumns()));

        tableName = null;
        databaseName = null;
        // 保存事件基础信息到数据库
        baseExpDao.updateById(eventRecord, tableSuffix);

        // 记录删除的行
        List<Serializable[]> rows = deleteRowsEventData.getRows();
        List<DeleteRowRecord> insertRowRecords = new ArrayList<>(rows.size());
        for (Serializable[] row : rows) {
            insertRowRecords.add(new DeleteRowRecord(eventRecord.getId(), gson.toJson(row)));
        }
        baseExpDao.batchInsert(insertRowRecords, tableSuffix);

        // 保存到自己写的数据库
        try {
            insertYanysql(event);
        } catch (Exception e) {
            log.error("保存到自己写的数据库发生异常", e);
        }

        return eventRecord;
    }

    private void insertYanysql(Event event){
        DeleteRowsEventData deleteRowsEventData = event.getData();
        currRecord.setTableId(String.valueOf(deleteRowsEventData.getTableId()));
        currRecord.setIncludedColumns(deleteRowsEventData.getIncludedColumns().toString());
        currRecord.setStep(StepEnum.SAVE_EVENT.name());
        currRecord.setTableName(tableName);
        currRecord.setDatabaseName(databaseName);
        currRecord.setIncludedColumnNames(getColumnNames(deleteRowsEventData.getIncludedColumns()));
        currRecord.setUpdateTime(new Date());
        // 保存事件基础信息到数据库
        yanySqlBaseDAO.updateById(currRecord);

        // 记录删除的行
        List<Serializable[]> rows = deleteRowsEventData.getRows();
        for (Serializable[] row : rows) {
            TDeleteRowRecord tDeleteRowRecord = new TDeleteRowRecord(currRecord.getId(), gson.toJson(row));
            tDeleteRowRecord.setId(UUID.randomUUID().toString());
            tDeleteRowRecord.setCreateTime(new Date());
            tDeleteRowRecord.setTenantId(tableSuffix);
           yanySqlBaseDAO.insert(tDeleteRowRecord);
        }
    }


}
