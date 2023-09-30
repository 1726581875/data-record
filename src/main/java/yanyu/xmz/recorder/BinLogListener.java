package yanyu.xmz.recorder;

import com.github.shyiko.mysql.binlog.event.EventHeader;
import yanyu.xmz.recorder.business.dao.YanySqlBaseDAO;
import yanyu.xmz.recorder.business.entity.event.*;
import yanyu.xmz.recorder.business.entity.yanysql.*;
import yanyu.xmz.recorder.business.handler.DbEventHandler;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.util.PropertiesReaderUtil;
import yanyu.xmz.recorder.business.handler.factory.HandlerFactory;
import yanyu.xmz.recorder.test.MyBinaryLogClient;

import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2022/5/7
 */
public class BinLogListener {


    private static final String hostname;

    private static final Integer port;

    private static final String username;

    private static final String password;

    private static final String schema;

    private static final YanySqlBaseDAO yanySqlBaseDAO = new YanySqlBaseDAO();

    static {
        hostname = PropertiesReaderUtil.get("mysql.monitor.hostname");
        port = Integer.valueOf(PropertiesReaderUtil.get("mysql.monitor.port"));
        username = PropertiesReaderUtil.get("mysql.monitor.username");
        password = PropertiesReaderUtil.get("mysql.monitor.password");
        schema = PropertiesReaderUtil.get("mysql.monitor.schema");

    }


    public static void main(String[] args) {

        // 删除表
        dropTables();

        // 初始化表
        initTable();

        MyBinaryLogClient client = new MyBinaryLogClient(hostname, port, schema, username, password);

        // 查询最新一条记录的binlog位置
/*        EventRecord lastRecord = getLastRecord();
        if(Objects.nonNull(lastRecord)){
            client.setBinlogFilename(lastRecord.getBinLogFileName());
            client.setBinlogPosition(lastRecord.getEndLogPos());
        }*/


        // 删除表
        yanySqlBaseDAO.dropTableIfExist(TEventRecord.class);
        yanySqlBaseDAO.dropTableIfExist(TDeleteRowRecord.class);
        yanySqlBaseDAO.dropTableIfExist(TInsertRowRecord.class);
        yanySqlBaseDAO.dropTableIfExist(TQueryEventRecord.class);
        yanySqlBaseDAO.dropTableIfExist(TUpdateRowRecord.class);
        // 创建表
        yanySqlBaseDAO.createTable(TEventRecord.class);
        yanySqlBaseDAO.createTable(TDeleteRowRecord.class);
        yanySqlBaseDAO.createTable(TInsertRowRecord.class);
        yanySqlBaseDAO.createTable(TQueryEventRecord.class);
        yanySqlBaseDAO.createTable(TUpdateRowRecord.class);

         // 固定位置读取
        client.setBinlogFilename("mysql-bin.000001");
        //client.setBinlogPosition(429220L);
        client.setBinlogPosition(4L);

        // 注册
        client.registerEventListener(event -> {
            EventHeader header = event.getHeader();
            DbEventHandler handler = HandlerFactory.getHandler(header.getEventType());
            if (handler != null) {
                handler.saveEvent(client.getBinlogPosition(), client.getBinlogFilename(),"", event);
            }
        });
        // 连接
        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void initTable() {
        // 若记录表不存在，则先创建表
        BaseDAO.mysqlInstance().createTableIfNotExist(EventRecord.class);
        BaseDAO.mysqlInstance().createTableIfNotExist(UpdateRowRecord.class);
        BaseDAO.mysqlInstance().createTableIfNotExist(DeleteRowRecord.class);
        BaseDAO.mysqlInstance().createTableIfNotExist(InsertRowRecord.class);
        BaseDAO.mysqlInstance().createTableIfNotExist(QueryEventRecord.class);
    }

    private static void dropTables() {
        BaseDAO.mysqlInstance().dropTableIfExist(EventRecord.class);
        BaseDAO.mysqlInstance().dropTableIfExist(UpdateRowRecord.class);
        BaseDAO.mysqlInstance().dropTableIfExist(DeleteRowRecord.class);
        BaseDAO.mysqlInstance().dropTableIfExist(InsertRowRecord.class);
        BaseDAO.mysqlInstance().dropTableIfExist(QueryEventRecord.class);
    }

    private static EventRecord getLastRecord(){
        return BaseDAO.mysqlInstance()
                .getOne("select * from event_record where " +
                        "end_log_pos is not null " +
                        "order by create_time desc limit 1", EventRecord.class);
    }


}
