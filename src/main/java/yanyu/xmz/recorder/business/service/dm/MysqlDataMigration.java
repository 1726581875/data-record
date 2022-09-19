package yanyu.xmz.recorder.business.service.dm;

import yanyu.xmz.recorder.business.dao.BaseDAO;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2022/9/5
 */
public class MysqlDataMigration {

    private BaseDAO sourceBaseDAO;

    private BaseDAO targetBaseDAO;

    public MysqlDataMigration(BaseDAO sourceBaseDAO, BaseDAO targetBaseDAO) {
        this.sourceBaseDAO = sourceBaseDAO;
        this.targetBaseDAO = targetBaseDAO;
    }

    public void syncAllTables(String suffix) {

        List<String> tableNameList = sourceBaseDAO.getList("show tables", String.class);

        if(tableNameList == null || tableNameList.size() == 0){
            System.out.println("该数据库下没有表，结束");
            return;
        }

        for (String tableName : tableNameList) {
            System.out.println("========= 表:" + tableName + " 开始 ==========");
            syncTable(tableName, suffix);
            System.out.println("========= 表:" + tableName + " 结束 ==========");
        }

    }


    public void syncTable(String tableName, String suffix) {
        syncTableMetadata(tableName, suffix);
        syncTableData(tableName, suffix);
    }


    private void syncTableMetadata(String tableName, String suffix) {
        // 获取远程mysql建表语句
        Map<String, Object> resultMap = sourceBaseDAO.getOne("show create table " + tableName, Map.class);

        // 本地若存在该表则先删除
        targetBaseDAO.exec("DROP TABLE IF EXISTS `" + tableName + "`");
        // 本地新建该表
        String createTableSql = String.valueOf(resultMap.get("Create Table")).replaceFirst(tableName, tableName + suffix);
        targetBaseDAO.exec(createTableSql);
    }

    private void syncTableData(String tableName, String suffix) {

        Long batchMaxNum = 10000L;

        Long count = sourceBaseDAO.getOne("select count(*) from " + tableName, Long.class);

        Long pageNum = count % batchMaxNum == 0L ? count / batchMaxNum : (count / batchMaxNum) + 1L;

        for (Long i = 1L; i <= pageNum; i++) {
            // 查询
            List<Map> resultMapList = sourceBaseDAO.getList("select * from `" + tableName
                            + "` limit " + batchMaxNum + " offset " + (pageNum - 1) * batchMaxNum, Map.class);
            // 数据入库本地数据库
            targetBaseDAO.batchInsert(tableName + suffix, resultMapList);
        }
    }

}
