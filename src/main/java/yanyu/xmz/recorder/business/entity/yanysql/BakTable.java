package yanyu.xmz.recorder.business.entity.yanysql;

import yanyu.xmz.recorder.business.dao.annotation.Id;
import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 * 记录所有备份表信息
 */
public class BakTable {

    @Id
    private String id;

    private String path;

    private String tableName;

    private String createTableSql;

    private Long rowNum;

    private Date createTime;

    private Date updateTime;

    public BakTable() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getRowNum() {
        return rowNum;
    }

    public void setRowNum(Long rowNum) {
        this.rowNum = rowNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTableSql() {
        return createTableSql;
    }

    public void setCreateTableSql(String createTableSql) {
        this.createTableSql = createTableSql;
    }
}
