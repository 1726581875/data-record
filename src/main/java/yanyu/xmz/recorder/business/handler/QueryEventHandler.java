package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.QueryEventData;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.entity.event.EventRecord;
import yanyu.xmz.recorder.business.entity.event.QueryEventRecord;

/**
 * @author xiaomingzhang
 * @date 2022/7/6
 */
public class QueryEventHandler extends AbstractMysqlEventHandler {

    @Override
    protected EventRecord saveEventDetailToDatabase(Event event, EventRecord eventRecord) {
        BaseDAO.mysqlInstance().insert(getQueryEventRecord(event, eventRecord.getId()));
        QueryEventData data = event.getData();

        // 处理mysql元数据变更
/*        if(data.getSql().contains("ALTER TABLE") || data.getSql().contains("alter table")) {
            String[] sqlArr = data.getSql().split(";");
            for (String sql : sqlArr) {
                if(sql.contains("ALTER TABLE") || data.getSql().contains("alter table")) {
                    try {
                        MysqlMetadataChangeHandler.analyzeTableFieldChange(sql, data.getDatabase());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }*/
        return null;
    }

    private QueryEventRecord getQueryEventRecord(Event event, Long recordId) {
        QueryEventData data = event.getData();
        QueryEventRecord queryRecord = new QueryEventRecord();
        queryRecord.setRecordId(recordId);
        queryRecord.setDatabaseName(data.getDatabase());
        queryRecord.setExecSql(data.getSql());
        queryRecord.setErrorCode(data.getErrorCode());
        queryRecord.setExecutionTime(data.getExecutionTime());
        queryRecord.setThreadId(data.getThreadId());
        return queryRecord;
    }

}
