package yanyu.xmz.recorder;

import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.util.ConnectUtil;
import yanyu.xmz.recorder.business.entity.metadata.MysqlColumn;
import yanyu.xmz.recorder.business.entity.metadata.MysqlSchema;
import yanyu.xmz.recorder.business.entity.metadata.MysqlTable;
import yanyu.xmz.recorder.test.BaseTest;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/8/10
 */
public class MetadataTest extends BaseTest {


    public static void main(String[] args) {

        // local
        ConnectUtil.Config config = ConnectUtil.getConfig();


        ConnectUtil.setConfig(new ConnectUtil.Config(hostname,port, "INFORMATION_SCHEMA", username,password));

        // 查询数据库List
        List<MysqlSchema> mysqlSchemataList = BaseDAO.mysqlInstance()
                .getList("select * from INFORMATION_SCHEMA.SCHEMATA", MysqlSchema.class);
        // 查询表List
        List<MysqlTable> mysqlTableList = BaseDAO.mysqlInstance()
                .getList("select * from INFORMATION_SCHEMA.TABLES", MysqlTable.class);
        // 查询列字段List
        List<MysqlColumn> mysqlColumnList = BaseDAO.mysqlInstance()
                .getList("select * from INFORMATION_SCHEMA.COLUMNS", MysqlColumn.class);



        // 切换回local连接
        ConnectUtil.setConfig(config);



        BaseDAO.mysqlInstance().dropTableIfExist(MysqlSchema.class);
        BaseDAO.mysqlInstance().dropTableIfExist(MysqlTable.class);
        BaseDAO.mysqlInstance().dropTableIfExist(MysqlColumn.class);

        BaseDAO.mysqlInstance().createTableIfNotExist(MysqlSchema.class);
        BaseDAO.mysqlInstance().createTableIfNotExist(MysqlTable.class);
        BaseDAO.mysqlInstance().createTableIfNotExist(MysqlColumn.class);

        BaseDAO.mysqlInstance().batchInsert(mysqlSchemataList);
        BaseDAO.mysqlInstance().batchInsert(mysqlTableList);
        BaseDAO.mysqlInstance().batchInsert(mysqlColumnList);


    }


}
