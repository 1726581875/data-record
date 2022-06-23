package yanyu.xmz.recorder.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import yanyu.xmz.recorder.dao.BaseDAO;
import yanyu.xmz.recorder.entity.EventRecord;
import yanyu.xmz.recorder.enums.StepEnum;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 */
public class TableMapEventHandler extends AbstractMysqlEventHandler {

    @Override
    protected EventRecord saveEventDetailToDatabase(Event event, EventRecord eventRecord) {

        TableMapEventData tableMapEventData = event.getData();
        eventRecord.setTableId(String.valueOf(tableMapEventData.getTableId()));
        eventRecord.setDatabaseName(tableMapEventData.getDatabase());
        eventRecord.setTableName(tableMapEventData.getTable());
        eventRecord.setStep(StepEnum.SAVE_DATA_SUCCESS.name());
        BaseDAO.mysqlInstance().updateById(eventRecord);

        return eventRecord;
    }
}
