import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.MysqlBaseDAO;
import yanyu.xmz.recorder.business.dao.util.PropertiesReaderUtil;
import yanyu.xmz.recorder.business.entity.DeleteRowRecord;
import yanyu.xmz.recorder.business.entity.EventRecord;
import yanyu.xmz.recorder.business.entity.InsertRowRecord;
import yanyu.xmz.recorder.business.entity.UpdateRowRecord;
import yanyu.xmz.recorder.handler.*;

import java.io.IOException;
import java.util.Objects;

/**
 * @author xiaomingzhang
 * @date 2022/5/9
 */
public class BinLogSimpleTest {


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

        // 若记录表不存在，则先创建表
        BaseDAO baseDAO = new MysqlBaseDAO();
        baseDAO.createTableIfNotExist(EventRecord.class);
        baseDAO.createTableIfNotExist(UpdateRowRecord.class);
        baseDAO.createTableIfNotExist(DeleteRowRecord.class);
        baseDAO.createTableIfNotExist(InsertRowRecord.class);


        BinaryLogClient client = new BinaryLogClient(hostname, port,username, password);
        client.setServerId(2);

        // 查询最新一条记录的binlog位置
        EventRecord lastRecord = BaseDAO.mysqlInstance()
                .getOne("select * from event_record where end_log_pos is not null order by create_time desc limit 1", EventRecord.class);
        if(Objects.nonNull(lastRecord)){
            client.setBinlogFilename(lastRecord.getBinLogFileName());
            client.setBinlogPosition(lastRecord.getEndLogPos());
        }

        client.setBinlogFilename("mysql-bin.000001");
        client.setBinlogPosition(415037L);



        client.registerEventListener(event -> {

            EventData data = event.getData();

            if (data instanceof TableMapEventData) {
                System.out.println("Table:");
                TableMapEventData tableMapEventData = (TableMapEventData) data;
                System.out.println(tableMapEventData.getTableId()+": ["+tableMapEventData.getDatabase() + "-" + tableMapEventData.getTable()+"]");
            }

            if (data instanceof UpdateRowsEventData) {
                System.out.println("Update:");
                System.out.println(data.toString());
            } else if (data instanceof WriteRowsEventData) {
                System.out.println("Insert:");
                System.out.println(data.toString());
            } else if (data instanceof DeleteRowsEventData) {
                System.out.println("Delete:");
                System.out.println(data.toString());
            }

        });

        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
