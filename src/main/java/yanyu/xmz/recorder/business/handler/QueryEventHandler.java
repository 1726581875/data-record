package yanyu.xmz.recorder.business.handler;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.QueryEventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yanyu.xmz.recorder.business.entity.event.EventRecord;
import yanyu.xmz.recorder.business.entity.event.QueryEventRecord;
import yanyu.xmz.recorder.business.entity.yanysql.TQueryEventRecord;

import java.util.Date;
import java.util.UUID;

/**
 * @author xiaomingzhang
 * @date 2022/7/6
 */
public class QueryEventHandler extends AbstractMysqlEventHandler {

    private static final Logger log = LoggerFactory.getLogger(QueryEventHandler.class);

    @Override
    protected EventRecord saveEventDetailToDatabase(Event event, EventRecord eventRecord) {
        baseExpDao.insert(getQueryEventRecord(event, eventRecord.getId()), tableSuffix);
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


        // 保存到自己写的数据库
        try {
            insertYanysql(event);
        } catch (Exception e) {
            log.error("保存到自己写的数据库发生异常", e);
        }

        return null;
    }

    private void insertYanysql(Event event) {
        QueryEventData data = event.getData();
        TQueryEventRecord queryRecord = new TQueryEventRecord();
        queryRecord.setId(UUID.randomUUID().toString());
        queryRecord.setRecordId(currRecord.getId());
        queryRecord.setDatabaseName(data.getDatabase());
        queryRecord.setExecSql(data.getSql());
        queryRecord.setErrorCode(data.getErrorCode());
        queryRecord.setExecutionTime(data.getExecutionTime());
        queryRecord.setThreadId(data.getThreadId());
        queryRecord.setCreateTime(new Date());
        queryRecord.setTenantId(tableSuffix);
        yanySqlBaseDAO.insert(queryRecord);
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
