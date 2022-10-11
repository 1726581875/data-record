package yanyu.xmz.recorder.business.service.impl;

import com.github.shyiko.mysql.binlog.event.EventHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.entity.event.*;
import yanyu.xmz.recorder.business.handler.DbEventHandler;
import yanyu.xmz.recorder.business.handler.factory.HandlerFactory;
import yanyu.xmz.recorder.business.model.entity.SysDataSource;
import yanyu.xmz.recorder.business.service.BinlogService;
import yanyu.xmz.recorder.business.service.DataSourceService;
import yanyu.xmz.recorder.test.MyBinaryLogClient;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/10/11
 */
@Service
public class BinlogServiceImpl implements BinlogService {

    private static final Logger log = LoggerFactory.getLogger(BinlogServiceImpl.class);

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public void listenBinlog(String tenantId, Long dataSourceId) {


        SysDataSource dataSource = dataSourceService.getDataSource(tenantId, dataSourceId);

        String tableSuffix = "_" + tenantId + "_" + dataSource;
        // 删除表
        dropTables(tableSuffix);
        // 初始化表
        initTable(tableSuffix);


        MyBinaryLogClient client = new MyBinaryLogClient(dataSource.getHostname(),
                Integer.valueOf(dataSource.getServerPort()), dataSource.getSchemaName(),
                dataSource.getUsername(), dataSource.getPassword());

        client.registerEventListener(event -> {
            EventHeader header = event.getHeader();
            DbEventHandler handler = HandlerFactory.getHandler(header.getEventType());
            if (handler != null) {
                handler.saveEvent(client.getBinlogPosition(), client.getBinlogFilename(), event);
            }
        });
        // 连接
        try {
            client.connect();
        } catch (IOException e) {
            log.error("解析binlog日志发生异常", e);
        }
    }


    private static void initTable(String suffix) {
        // 若记录表不存在，则先创建表
        BaseDAO.mysqlInstance().createTableIfNotExist(EventRecord.class, suffix);
        BaseDAO.mysqlInstance().createTableIfNotExist(UpdateRowRecord.class, suffix);
        BaseDAO.mysqlInstance().createTableIfNotExist(DeleteRowRecord.class, suffix);
        BaseDAO.mysqlInstance().createTableIfNotExist(InsertRowRecord.class, suffix);
        BaseDAO.mysqlInstance().createTableIfNotExist(QueryEventRecord.class, suffix);
    }

    private static void dropTables(String suffix) {
        BaseDAO.mysqlInstance().dropTableIfExist(EventRecord.class, suffix);
        BaseDAO.mysqlInstance().dropTableIfExist(UpdateRowRecord.class, suffix);
        BaseDAO.mysqlInstance().dropTableIfExist(DeleteRowRecord.class, suffix);
        BaseDAO.mysqlInstance().dropTableIfExist(InsertRowRecord.class, suffix);
        BaseDAO.mysqlInstance().dropTableIfExist(QueryEventRecord.class, suffix);
    }

    private static EventRecord getLastRecord(){
        return BaseDAO.mysqlInstance()
                .getOne("select * from event_record where " +
                        "end_log_pos is not null " +
                        "order by create_time desc limit 1", EventRecord.class);
    }



}
