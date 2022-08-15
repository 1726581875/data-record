package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.google.gson.Gson;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.entity.EventRecord;
import yanyu.xmz.recorder.business.entity.UpdateRowRecord;
import yanyu.xmz.recorder.business.enums.StepEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 */
public class UpdateEventHandler extends AbstractMysqlEventHandler {

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
        BaseDAO.mysqlInstance().updateById(eventRecord);

        // 保存更新事件变更记录
        List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
        List<UpdateRowRecord> updateRowRecords = new ArrayList<>(rows.size());
        Gson gson = new Gson();
        for (Map.Entry<Serializable[], Serializable[]> row : rows) {
            updateRowRecords.add(new UpdateRowRecord(eventRecord.getId(), gson.toJson(row.getKey()), gson.toJson(row.getValue())));
        }
        BaseDAO.mysqlInstance().batchInsert(updateRowRecords);
        return eventRecord;
    }
}
