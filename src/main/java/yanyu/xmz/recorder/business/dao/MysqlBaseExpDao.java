package yanyu.xmz.recorder.business.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yanyu.xmz.recorder.business.dao.util.ConnectUtil;
import yanyu.xmz.recorder.business.dao.util.ConnectionManagerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/10/11
 * 扩展类
 */
public class MysqlBaseExpDao extends MysqlBaseDAO {


    private static final Logger log = LoggerFactory.getLogger(MysqlBaseExpDao.class);


    public MysqlBaseExpDao(){

    }

    public MysqlBaseExpDao(ConnectUtil.Config config){
        super(config);
    }


    public <T> int insert(T obj, String suffix) {
        if (obj == null) {
            throw new RuntimeException("插入元素为空, 请检查参数");
        }
        String tableName = getTableName(obj) + suffix;
        List<String> columnNameList = getColumnNameList(obj);
        String insertSql = getInsertPrepareSQL(columnNameList, tableName);

        try (Connection conn = ConnectionManagerUtil.getConnection(super.getConfig());
             PreparedStatement statement = conn.prepareStatement(insertSql)) {
            return execInsert(statement, columnNameList, obj);
        } catch (Exception e) {
            log.error("插入失败", e);
            throw new RuntimeException("数据库插入失败:" + e.getMessage());
        }
    }


    public <T> Long insertReturnKey(T obj, String suffix) {
        if (obj == null) {
            throw new RuntimeException("插入元素为空, 请检查参数");
        }

        String tableName = getTableName(obj) + suffix;
        List<String> columnNameList = getColumnNameList(obj);
        String insertSql = getInsertPrepareSQL(columnNameList, tableName);

        try (Connection conn = ConnectionManagerUtil.getConnection(super.getConfig());
             PreparedStatement statement = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            execInsert(statement, columnNameList, obj);
            // 检索由于执行此 Statement 对象而创建的所有自动生成的键
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                log.error("插入数据，获取主键失败");
                throw new RuntimeException("无法获取到主键");
            }
        } catch (Exception e) {
            log.error("插入失败", e);
            throw new RuntimeException("数据库插入失败:" + e.getMessage());
        }
    }




}
