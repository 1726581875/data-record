package yanyu.xmz.recorder.business.handler.metadata;

import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.entity.metadata.MysqlColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/8/11
 */
public class MysqlMetadataChangeHandler {

    private static final String ALTER = "ALTER";
    private static final String TABLE = "TABLE";
    private static final String ADD = "ADD";
    private static final String DROP = "DROP";
    private static final String COLUMN = "COLUMN";
    private static final String AFTER = "AFTER";


    public static void main(String[] args) {
        analyzeTableFieldChange("ALTER TABLE `xmz_test`.`table1` ADD COLUMN `file2` varchar(255) NULL AFTER `field`", null);
    }

    /**
     * ALTER TABLE `xmz_test`.`table1` ADD COLUMN `file2` varchar(255) NULL AFTER `field`
     * ALTER TABLE `xmz_test`.`table1` DROP COLUMN `file2`
     * @return
     */
    public static void analyzeTableFieldChange(String sql,String dbName) {

        String[] split = sql.split("\\s+");
        String option = split[3];
        String optionType = split[4];
        if(!"COLUMN".equals(optionType) || !"column".equals(optionType)){
            return;
        }

        String[] names = split[2].split("\\.");
        String databaseName = null;
        String tableName = null;
        if(names.length == 2) {
             databaseName = removeChar(names[0], "`");
             tableName = removeChar(names[1], "`");
        } else {
             tableName = removeChar(names[0], "`");
             databaseName = dbName;
        }
        String columnName = removeChar(split[5],"`");


        String querySql = "select * from mysql_metadata where table_schema='" + databaseName + "' and table_name='" + tableName + "' order by ordinal_position";
        System.out.println(querySql);
        List<MysqlColumn> fieldMetadataList = BaseDAO.mysqlInstance().getList(querySql, MysqlColumn.class);
        List<MysqlColumn> updateColumnList = new ArrayList<>();
        switch (option.toUpperCase()) {
            // 新增字段
            case "ADD":
                MysqlColumn mysqlMetadata = new MysqlColumn();
                mysqlMetadata.setTableSchema(databaseName);
                mysqlMetadata.setTableName(tableName);
                mysqlMetadata.setColumnName(columnName);
                // 中级某个位置插入列
                if(sql.contains("AFTER") || sql.contains("after")) {
                    String beforeColumn = removeChar(split[split.length -1], "`");
                    long index = 0L;
                    for (MysqlColumn metadata: fieldMetadataList) {
                        if(metadata.getColumnName().equals(beforeColumn)) {
                            mysqlMetadata.setOrdinalPosition(metadata.getOrdinalPosition() + 1L);
                            index = metadata.getOrdinalPosition();
                        }
                        // 插入位置之后的列整体往后移动
                        if(index != 0L && metadata.getOrdinalPosition() > index) {
                            metadata.setOrdinalPosition(metadata.getOrdinalPosition() + 1L);
                            updateColumnList.add(metadata);
                        }
                    }
                }else {
                    // 最后位置插入字段
                    mysqlMetadata.setOrdinalPosition((long) fieldMetadataList.size() + 1);
                }

                BaseDAO.mysqlInstance().insert(mysqlMetadata);
                // todo 后续优化，批量更新
                updateColumnList.forEach(e -> BaseDAO.mysqlInstance().updateById(e));

                break;
            // 删除字段
            case "DROP":
                Long ordinalPosition = null;
                for (MysqlColumn metadata: fieldMetadataList) {
                    // 删除对应列
                    if(metadata.getColumnName().equals(columnName)) {
                        ordinalPosition = metadata.getOrdinalPosition();
                        BaseDAO.mysqlInstance().deleteById(MysqlColumn.class, metadata.getId());
                    }
                }
                // 被删除列位置之后的列往前移动一位
                if(ordinalPosition != null) {
                    for (MysqlColumn metadata: fieldMetadataList) {
                        if(metadata.getOrdinalPosition() > ordinalPosition) {
                            metadata.setOrdinalPosition(metadata.getOrdinalPosition() - 1L);
                            // todo 后续优化，批量更新
                            BaseDAO.mysqlInstance().updateById(metadata);
                        }
                    }
                }
                break;

            case "CHANGE":

                break;


            default:
                throw new IllegalArgumentException("操作不允许,option=" + option + ", sql=" + sql);
        }

    }


    private static String removeChar(String str, String c) {
        if(str == null){
            return str;
        }
        return str.replace(c, "");
    }





}
