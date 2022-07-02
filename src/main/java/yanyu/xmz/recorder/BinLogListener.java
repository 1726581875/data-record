package yanyu.xmz.recorder;

import com.github.shyiko.mysql.binlog.event.EventData;
import yanyu.xmz.recorder.business.handler.DbEventHandler;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.util.PropertiesReaderUtil;
import yanyu.xmz.recorder.business.entity.EventRecord;
import yanyu.xmz.recorder.business.entity.DeleteRowRecord;
import yanyu.xmz.recorder.business.entity.InsertRowRecord;
import yanyu.xmz.recorder.business.entity.UpdateRowRecord;
import yanyu.xmz.recorder.business.handler.factory.HandlerFactory;
import yanyu.xmz.recorder.test.MyBinaryLogClient;

import java.io.IOException;
import java.util.Objects;

/**
 * @author xiaomingzhang
 * @date 2022/5/7
 */
public class BinLogListener {


    private static final String hostname;

    private static final Integer port;

    private static final String username;

    private static final String password;

    static {
        hostname = PropertiesReaderUtil.get("mysql.listener.hostname");
        port = Integer.valueOf(PropertiesReaderUtil.get("mysql.listener.port"));
        username = PropertiesReaderUtil.get("mysql.listener.username");
        password = PropertiesReaderUtil.get("mysql.listener.password");
    }


    public static void main(String[] args) {

        // 初始化表
        initTable();

        MyBinaryLogClient client = new MyBinaryLogClient(hostname, port,username, password);

        // 查询最新一条记录的binlog位置
        EventRecord lastRecord = BaseDAO.mysqlInstance()
                .getOne("select * from event_record where end_log_pos is not null order by create_time desc limit 1", EventRecord.class);
        if(Objects.nonNull(lastRecord)){
            client.setBinlogFilename(lastRecord.getBinLogFileName());
            client.setBinlogPosition(lastRecord.getEndLogPos());
        }

        // 注册
        client.registerEventListener(event -> {
            EventData data = event.getData();
            if(data != null) {
                DbEventHandler handler = HandlerFactory.getHandler(data.getClass());
                if (handler != null) {
                    handler.saveEvent(client.getBinlogPosition(), event);
                }
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
    }


}
