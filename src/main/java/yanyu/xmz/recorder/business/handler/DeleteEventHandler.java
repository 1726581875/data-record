package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.*;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.entity.event.EventRecord;
import yanyu.xmz.recorder.business.entity.event.DeleteRowRecord;
import yanyu.xmz.recorder.business.enums.StepEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 */
public class DeleteEventHandler extends AbstractMysqlEventHandler {


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
        return eventRecord;
    }
}
