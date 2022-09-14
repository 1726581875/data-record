package yanyu.xmz.recorder.test;

import yanyu.xmz.recorder.business.dao.MysqlBaseDAO;
import yanyu.xmz.recorder.business.dao.util.ConnectUtil;

import java.util.List;
import java.util.Map;

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


        List<String> tableNameList = devBaseDAO.getList("show tables", String.class);

        if(tableNameList == null || tableNameList.size() == 0){
            System.out.println("该数据库下没有表，结束");
            return;
        }

        for (String tableName : tableNameList) {
            System.out.println("========= 表:" + tableName + " 开始 ==========");

            // 获取远程mysql建表语句
            Map<String,Object> resultMap = devBaseDAO.getOne("show create table " + tableName, Map.class);


            // 本地若存在该表则先删除
            localBaseDAO.exec("DROP TABLE IF EXISTS `"+ tableName +"`");
            // 本地新建该表
            localBaseDAO.exec((String) resultMap.get("Create Table"));

            // 全量获取数据列表,todo 之后改为分批
            List<Map> resultMapList = devBaseDAO.getList("select * from " + tableName, Map.class);

            // 数据入库本地数据库
            localBaseDAO.batchInsert(tableName, resultMapList);

            System.out.println("========= 表:" + tableName + " 结束 ==========");
        }


    }


}
