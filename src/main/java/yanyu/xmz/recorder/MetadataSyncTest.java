package yanyu.xmz.recorder;

import yanyu.xmz.recorder.business.dao.MysqlBaseDAO;
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
public class MetadataSyncTest extends BaseTest {


    public static void main(String[] args) {

        ConnectUtil.Config devConfig = ConnectUtil.getConfig("dev");
        ConnectUtil.Config localConfig = ConnectUtil.getConfig("local");

        MysqlBaseDAO devBaseDAO = new MysqlBaseDAO(devConfig);
        MysqlBaseDAO localBaseDAO = new MysqlBaseDAO(localConfig);

        // 查询数据库List
        List<MysqlSchema> mysqlSchemataList = devBaseDAO.getList("select * from INFORMATION_SCHEMA.SCHEMATA", MysqlSchema.class);
        // 查询表List
        List<MysqlTable> mysqlTableList = devBaseDAO.getList("select * from INFORMATION_SCHEMA.TABLES", MysqlTable.class);
        // 查询列字段List
        List<MysqlColumn> mysqlColumnList = devBaseDAO.getList("select * from INFORMATION_SCHEMA.COLUMNS", MysqlColumn.class);



        localBaseDAO.dropTableIfExist(MysqlSchema.class);
        localBaseDAO.dropTableIfExist(MysqlTable.class);
        localBaseDAO.dropTableIfExist(MysqlColumn.class);

        localBaseDAO.createTableIfNotExist(MysqlSchema.class);
        localBaseDAO.createTableIfNotExist(MysqlTable.class);
        localBaseDAO.createTableIfNotExist(MysqlColumn.class);

        localBaseDAO.batchInsert(mysqlSchemataList);
        localBaseDAO.batchInsert(mysqlTableList);
        localBaseDAO.batchInsert(mysqlColumnList);


    }


}
