package yanyu.xmz.recorder;

import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.util.ConnectUtil;

import java.util.List;
import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2022/8/24
 */
public class DataMigrationTest {

    public static void main(String[] args) {

        ConnectUtil.changeDataSource("monitor");
        List<String> tableNameList = BaseDAO.mysqlInstance().getList("show tables", String.class);

        if(tableNameList == null || tableNameList.size() == 0){
            System.out.println("该数据库下没有表，结束");
            return;
        }

        for (String tableName : tableNameList) {
            System.out.println("========= 表:" + tableName + " 开始 ==========");

            ConnectUtil.changeDataSource("monitor");
            // 获取远程mysql建表语句
            Map<String,Object> resultMap = BaseDAO.mysqlInstance().getOne("show create table " + tableName, Map.class);

            // 切换到本地数据库
            ConnectUtil.changeDataSource("local");
            // 本地若存在该表则先删除
            BaseDAO.mysqlInstance().exec("DROP TABLE IF EXISTS `"+ tableName +"`");
            // 本地新建该表
            BaseDAO.mysqlInstance().exec((String) resultMap.get("Create Table"));

            // 切换到远程mysql数据源
            ConnectUtil.changeDataSource("monitor");
            // 全量获取数据列表,todo 之后改为分批
            List<Map> resultMapList = BaseDAO.mysqlInstance().getList("select * from " + tableName, Map.class);

            // 切换到本地数据源
            ConnectUtil.changeDataSource("local");
            // 数据入库本地数据库
            BaseDAO.mysqlInstance().batchInsert(tableName, resultMapList);

            System.out.println("========= 表:" + tableName + " 结束 ==========");
        }


    }


}
