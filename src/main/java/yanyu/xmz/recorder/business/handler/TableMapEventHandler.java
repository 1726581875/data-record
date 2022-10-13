package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.entity.event.EventRecord;
import yanyu.xmz.recorder.business.enums.StepEnum;

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
        eventRecord.setStep(StepEnum.SAVE_SUCCESS.name());
        baseExpDao.updateById(eventRecord, tableSuffix);


        tableName = tableMapEventData.getTable();
        databaseName = tableMapEventData.getDatabase();

        return eventRecord;
    }
}
