package yanyu.xmz.recorder;

import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.util.ConnectUtil;
import yanyu.xmz.recorder.business.entity.metadata.MysqlColumn;
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


        String url = "jdbc:mysql://"+ hostname +":"+ port+"/mooc?characterEncoding=UTF-8&serverTimezone=GMT%2B8";
        ConnectUtil.setConfig(new ConnectUtil.Config(url,username,password,ConnectUtil.MYSQL_DRIVER));

        List<MysqlColumn> mysqlMetadataList = BaseDAO.mysqlInstance()
                .getList("select * from INFORMATION_SCHEMA.COLUMNS", MysqlColumn.class);


        ConnectUtil.setConfig(config);
        BaseDAO.mysqlInstance().dropTable(MysqlColumn.class);
        BaseDAO.mysqlInstance().createTableIfNotExist(MysqlColumn.class);

        BaseDAO.mysqlInstance().batchInsert(mysqlMetadataList);


    }


}
