package yanyu.xmz.recorder.business.service.dm;

import yanyu.xmz.recorder.business.dao.MysqlBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2022/9/5
 */
public class MysqlDataMigration {

    private MysqlBaseDAO devBaseDAO;

    private MysqlBaseDAO localBaseDAO;


    public void syncAllTables() {

        List<String> tableNameList = devBaseDAO.getList("show tables", String.class);

        if(tableNameList == null || tableNameList.size() == 0){
            System.out.println("该数据库下没有表，结束");
            return;
        }

        for (String tableName : tableNameList) {
            System.out.println("========= 表:" + tableName + " 开始 ==========");
            syncTable(tableName);
            System.out.println("========= 表:" + tableName + " 结束 ==========");
        }

    }


    public void syncTable(String tableName) {
        syncTableMetadata(tableName);
        syncTableData(tableName);
    }


    private void syncTableMetadata(String tableName) {
        // 获取远程mysql建表语句
        Map<String, Object> resultMap = devBaseDAO.getOne("show create table " + tableName, Map.class);

        // 本地若存在该表则先删除
        localBaseDAO.exec("DROP TABLE IF EXISTS `" + tableName + "`");
        // 本地新建该表
        localBaseDAO.exec((String) resultMap.get("Create Table"));
    }

    private void syncTableData(String tableName) {
        // 全量获取数据列表
        List<Map> resultMapList = devBaseDAO.getList("select * from " + tableName, Map.class);

        // 数据入库本地数据库
        localBaseDAO.batchInsert(tableName, resultMapList);
    }

}
