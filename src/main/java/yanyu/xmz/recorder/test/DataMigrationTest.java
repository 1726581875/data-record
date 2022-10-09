package yanyu.xmz.recorder.test;

import yanyu.xmz.recorder.business.dao.MysqlBaseDAO;
import yanyu.xmz.recorder.business.dao.util.ConnectUtil;
import yanyu.xmz.recorder.business.service.dm.MysqlDataMigration;


/**
 * @author xiaomingzhang
 * @date 2022/8/24
 */
public class DataMigrationTest {

    public static void main(String[] args) {

        ConnectUtil.Config devConfig = ConnectUtil.getConfig("dev");
        ConnectUtil.Config localConfig = ConnectUtil.getConfig("local");

        MysqlBaseDAO devBaseDAO = new MysqlBaseDAO(devConfig);
        MysqlBaseDAO localBaseDAO = new MysqlBaseDAO(localConfig);

        MysqlDataMigration dataMigration = new MysqlDataMigration(devBaseDAO, localBaseDAO);

        dataMigration.syncAllTables("_sync1");


    }


}
