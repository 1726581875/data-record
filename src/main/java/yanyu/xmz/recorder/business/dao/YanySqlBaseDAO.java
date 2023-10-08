package yanyu.xmz.recorder.business.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yanyu.xmz.recorder.business.dao.util.ConnectUtil;
import yanyu.xmz.recorder.business.dao.util.ConnectionManagerUtil;
import yanyu.xmz.recorder.business.entity.yanysql.TEventRecord;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;


/**
 * @author xiaomingzhang
 * @date 2023/9/11
 * 访问个人数据库
 */
public class YanySqlBaseDAO extends MysqlBaseDAO {

    private static final Logger log = LoggerFactory.getLogger(YanySqlBaseDAO.class);

    private ConnectUtil.Config yanyDbConfig =
            new ConnectUtil.Config("localhost:8888:record", null, null, "com.moyu.test.jdbc.Driver");


    public YanySqlBaseDAO() {
        config = yanyDbConfig;
    }


    @Override
    public boolean exec(String sql) {
        try (Connection conn = ConnectionManagerUtil.getConnection(config);
             Statement statement = conn.createStatement()) {
            return statement.execute(sql);
        } catch (Exception e) {
            log.error("执行失败", e);
            throw new RuntimeException("sql执行失败:" + e.getMessage());
        }
    }


    public static void main(String[] args) {
        YanySqlBaseDAO yanySqlBaseDAO = new YanySqlBaseDAO();
/*        yanySqlBaseDAO.exec("create table test (id int, name char)");
        yanySqlBaseDAO.exec("insert into test (id, name) value(1,'肖明章')");*/
        yanySqlBaseDAO.exec("drop table if exists t_event_record");
        yanySqlBaseDAO.createTable(TEventRecord.class);

        TEventRecord record = new TEventRecord();
        record.setId(UUID.randomUUID().toString());
        record.setCreateTime(new Date());
        yanySqlBaseDAO.insert(record);


    }

}
