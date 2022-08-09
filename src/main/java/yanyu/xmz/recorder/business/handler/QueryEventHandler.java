package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.QueryEventData;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.entity.EventRecord;
import yanyu.xmz.recorder.business.entity.QueryEventRecord;

/**
 * @author xiaomingzhang
 * @date 2022/7/6
 */
public class QueryEventHandler extends AbstractMysqlEventHandler {

    @Override
    protected EventRecord saveEventDetailToDatabase(Event event, EventRecord eventRecord) {
        BaseDAO.mysqlInstance().insert(getQueryEventRecord(event, eventRecord.getId()));
        return null;
    }

    private QueryEventRecord getQueryEventRecord(Event event, Long recordId) {
        QueryEventData data = event.getData();
        QueryEventRecord queryRecord = new QueryEventRecord();
        queryRecord.setRecordId(recordId);
        queryRecord.setDatabase(data.getDatabase());
        queryRecord.setSql(data.getSql());
        queryRecord.setErrorCode(data.getErrorCode());
        queryRecord.setExecutionTime(data.getExecutionTime());
        queryRecord.setThreadId(data.getThreadId());
        return queryRecord;
    }

}
