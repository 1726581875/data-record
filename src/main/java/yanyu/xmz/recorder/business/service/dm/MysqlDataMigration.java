package yanyu.xmz.recorder.business.service.dm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.YanySqlBaseDAO;
import yanyu.xmz.recorder.business.dao.util.PropertiesReaderUtil;
import yanyu.xmz.recorder.business.entity.yanysql.BakTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author xiaomingzhang
 * @date 2022/9/5
 */
public class MysqlDataMigration {

    private static final Logger log = LoggerFactory.getLogger(MysqlDataMigration.class);

    private BaseDAO sourceBaseDAO;

    private BaseDAO targetBaseDAO;

    private YanySqlBaseDAO bakYanyDatabaseDAO = new YanySqlBaseDAO(PropertiesReaderUtil.get("yanysql.data_bak.url"));

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
            log.info("========= 表:" + tableName + " 开始 ==========");
            syncTable(tableName, suffix);
            log.info("========= 表:" + tableName + " 结束 ==========");
        }

    }


    public Long syncTable(String tableName, String suffix) {
        String bakTableName = syncTableMetadata(tableName, suffix);
        Long count = syncTableData(tableName, suffix, bakTableName);
        return count;
    }


    private String syncTableMetadata(String tableName, String suffix) {
        // 获取远程mysql建表语句
        Map<String, Object> resultMap = sourceBaseDAO.getOne("show create table " + tableName, Map.class);

        // 本地mysql若存在该表则先删除
        targetBaseDAO.exec("DROP TABLE IF EXISTS `" + tableName + suffix + "`");
        // 本地mysql新建该表
        String createTableSql = String.valueOf(resultMap.get("Create Table")).replaceFirst(tableName, tableName + suffix);
        targetBaseDAO.exec(createTableSql);

        // 在个人数据库(yanysql)创建备份表
        String bakTableName = null;
        try {
            // 创建表
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            bakTableName = tableName + suffix + dateFormat.format(new Date());
            String bakTableSql = String.valueOf(resultMap.get("Create Table")).replaceFirst(tableName, bakTableName);
            bakYanyDatabaseDAO.exec(bakTableSql);

            // 插入建表记录
            BakTable bakTable = new BakTable();
            bakTable.setId(UUID.randomUUID().toString().replace("-", ""));
            bakTable.setCreateTime(new Date());
            bakTable.setPath(suffix);
            bakTable.setTableName(bakTableName);
            bakTable.setCreateTableSql(bakTableSql);
            bakYanyDatabaseDAO.insert(bakTable);
        } catch (Exception e) {
            log.error("创建yany数据库备份表发生异常", e);
        }
        return bakTableName;
    }


    /**
     *
     * todo 1、只通过简单分批读取然后插入，导致只能用于数据不持续增加/修改的情况，如果数据持续新增/修改，无法做到同步某一刻的数据
     * todo 2、大表数据同步慢，一千多万的表几乎三小时
     * todo 3、会影响被源数据库的性能
     * @param tableName
     * @param suffix
     */
    private Long syncTableData(String tableName, String suffix, String bakTableName) {

        Long batchMaxNum = 10000L;

        Long count = sourceBaseDAO.getOne("select count(*) from " + tableName, Long.class);

        Long pageNum = count % batchMaxNum == 0L ? count / batchMaxNum : (count / batchMaxNum) + 1L;

        long dataSyncStartTime = System.currentTimeMillis();
        for (Long i = 1L; i <= pageNum; i++) {
            long batchStartTime = System.currentTimeMillis();
            log.info("{}表,第{}页数据,一共{}页 limit={},开始", tableName, i, pageNum, batchMaxNum);
            // 查询
            List<Map> resultMapList = sourceBaseDAO.getList("select * from `" + tableName
                    + "` limit " + batchMaxNum + " offset " + (i - 1) * batchMaxNum, Map.class);

            // 数据入库本地数据库
            if (resultMapList != null && resultMapList.size() > 0) {
                targetBaseDAO.batchInsert(tableName + suffix, resultMapList);
            }

            // 插入到个人数据库
            if (bakTableName != null) {
                try {
                    bakYanyDatabaseDAO.batchInsert(bakTableName, resultMapList);
                } catch (Exception e) {
                    log.error("插入个人数据库发送异常,tableName={}", bakTableName, e);
                }
            }

            log.info("{}表,第{}页数据 size={},批次结束,耗时={}s", tableName, i,resultMapList == null ? 0 : resultMapList.size()
                    , (System.currentTimeMillis() - batchStartTime) / 1000);
        }

        log.info("{}表数据同步结束,数据量={},总耗时={}", tableName, count, (System.currentTimeMillis() - dataSyncStartTime) / 1000);

        return count;
    }

}
