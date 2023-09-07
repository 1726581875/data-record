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
import yanyu.xmz.recorder.business.model.RespResult;
import yanyu.xmz.recorder.business.model.entity.SysDataSource;
import yanyu.xmz.recorder.business.service.BinlogService;
import yanyu.xmz.recorder.business.service.DataSourceService;
import yanyu.xmz.recorder.test.MyBinaryLogClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaomingzhang
 * @date 2022/10/11
 */
@Service
public class BinlogServiceImpl implements BinlogService {

    private static final Logger log = LoggerFactory.getLogger(BinlogServiceImpl.class);

    @Autowired
    private DataSourceService dataSourceService;


    private Map<Long, MyBinaryLogClient> listenBinlogClientMap = new ConcurrentHashMap<>();


    @Override
    public RespResult listenBinlog(String tenantId, Long dataSourceId) {


        BinlogListenState listenState = BaseDAO.expDaoInstance().getOne("select * from binlog_listen_state where state = '1' " +
                "and tenant_id = ? and data_source_id = ? order by create_time desc limit 1", BinlogListenState.class, tenantId, dataSourceId);
        if(listenState == null) {
            log.warn("正在监听中,tenantId={}, dataSourceId={}",tenantId, dataSourceId);
            return RespResult.fail("正在监听中, 不能重复启用监听程序");
        }

        SysDataSource dataSource = dataSourceService.getDataSource(tenantId, dataSourceId);

        String tableSuffix = "_" + tenantId + "_" + dataSourceId;
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
                handler.saveEvent(client.getBinlogPosition(), client.getBinlogFilename(), tableSuffix, event);
            }
        });

        // 插入监听记录
        BinlogListenState binlogListenState = new BinlogListenState();
        binlogListenState.setTenantId(tenantId);
        binlogListenState.setDataSourceId(dataSourceId);
        binlogListenState.setState(BinlogListenState.STATE_LISTENING);
        Long listenId = BaseDAO.expDaoInstance().insertReturnKey(binlogListenState, "");
        binlogListenState.setId(listenId);

        Thread thread = new Thread(() -> {
            log.info("监听解析binlog【开始】 >>>> 租户id={},数据源id={}, listenId={}", tenantId, dataSourceId, listenId);
            // 连接
            try {
                client.connect();
            } catch (IOException e) {
                log.error("监听解析binlog发生异常,租户id={},数据源id={}", tenantId, dataSourceId, e);
                binlogListenState.setState(BinlogListenState.STATE_EXCEPTION);
                BaseDAO.expDaoInstance().updateById(binlogListenState, tableSuffix);
            }
            log.info("监听解析binlog【结束】 >>>> 租户id={},数据源id={}", tenantId, dataSourceId);
        }, "Thread-" + listenId);

        thread.setDaemon(true);
        thread.start();

        listenBinlogClientMap.put(listenId, client);

        return RespResult.success();
    }

    @Override
    public RespResult cancelListen(String tenantId, Long dataSourceId) {

        BinlogListenState listenState = BaseDAO.expDaoInstance().getOne("select * from binlog_listen_state where state = '1' " +
                "and tenant_id = ? and data_source_id = ? order by create_time desc limit 1", BinlogListenState.class, tenantId, dataSourceId);
        if(listenState == null) {
            log.warn("取消失败，监听线程不存在,tenantId={}, dataSourceId={}",tenantId, dataSourceId);
            return RespResult.fail("取消失败，数据库查询正在监听的记录不存在");
        }

        MyBinaryLogClient myBinaryLogClient = listenBinlogClientMap.get(listenState.getId());
        if(myBinaryLogClient == null) {
            log.warn("取消失败，监听线程不存在. 租户id={}, 数据源id={},listenId={}",tenantId, dataSourceId, listenState.getId());
            return RespResult.fail("取消失败，监听线程不存在");
        }

        try {
            myBinaryLogClient.disconnect();
        } catch (IOException e) {
            log.info("中断监听程序发生异常，租户id={}, 数据源id={},listenId={}", tenantId, dataSourceId, listenState.getId(), e);
            return RespResult.fail("中断监听程序发生异常");
        }

        listenState.setState(BinlogListenState.STATE_CANCEL);
        BaseDAO.expDaoInstance().updateById(listenState, "");

        log.info("中断监听程序成功，租户id={}, 数据源id={},listenId={}", tenantId, dataSourceId, listenState.getId());

        return RespResult.success();
    }


    private static void initTable(String suffix) {
        // 若记录表不存在，则先创建表
        BaseDAO.mysqlInstance().createTableIfNotExist(EventRecord.class, suffix);
        BaseDAO.mysqlInstance().createTableIfNotExist(UpdateRowRecord.class, suffix);
        BaseDAO.mysqlInstance().createTableIfNotExist(DeleteRowRecord.class, suffix);
        BaseDAO.mysqlInstance().createTableIfNotExist(InsertRowRecord.class, suffix);
        BaseDAO.mysqlInstance().createTableIfNotExist(QueryEventRecord.class, suffix);
        BaseDAO.mysqlInstance().createTableIfNotExist(BinlogListenState.class, "");
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
