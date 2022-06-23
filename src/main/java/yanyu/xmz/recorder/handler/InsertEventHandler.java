package yanyu.xmz.recorder.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import yanyu.xmz.recorder.dao.BaseDAO;
import yanyu.xmz.recorder.entity.EventRecord;
import yanyu.xmz.recorder.entity.InsertRowRecord;
import yanyu.xmz.recorder.enums.StepEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 */
public class InsertEventHandler extends AbstractMysqlEventHandler {

    @Override
    protected EventRecord saveEventDetailToDatabase(Event event, EventRecord eventRecord) {
        // 构造事件
        WriteRowsEventData writeRowsEventData = event.getData();
        eventRecord.setTableId(String.valueOf(writeRowsEventData.getTableId()));
        eventRecord.setIncludedColumns(writeRowsEventData.getIncludedColumns().toString());
        eventRecord.setStep(StepEnum.SAVE_EVENT_SUCCESS.name());
        // 保存事件详情信息
        BaseDAO.mysqlInstance().updateById(eventRecord);

        // 保存插入事件数据变更记录
        List<Serializable[]> rows = writeRowsEventData.getRows();
        List<InsertRowRecord> insertRowRecords = new ArrayList<>(rows.size());
        for (Serializable[] row : rows) {
            insertRowRecords.add(new InsertRowRecord(eventRecord.getId(), gson.toJson(row)));
        }
        BaseDAO.mysqlInstance().batchInsert(insertRowRecords);
        eventRecord.setStep(StepEnum.SAVE_DATA_SUCCESS.name());

        return eventRecord;
    }
}
