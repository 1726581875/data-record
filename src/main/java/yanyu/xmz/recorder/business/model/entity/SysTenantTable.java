package yanyu.xmz.recorder.business.model.entity;

import yanyu.xmz.recorder.business.dao.annotation.Id;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/9/13
 */
public class SysTenantTable {

    @Id
    private Long id;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 数据源id
     */
    private Long dataSourceId;
    /**
     * 源表表名
     */
    private String sourceTableName;
    /**
     * 同步过来后表名
     */
    private String tableName;
    /**
     * 表的数据量
     */
    private Long rowNum;

    private Date createTime;

    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }


    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public Long getRowNum() {
        return rowNum;
    }

    public void setRowNum(Long rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String toString() {
        return "SysTenantTable{" +
                "id=" + id +
                ", tenantId='" + tenantId + '\'' +
                ", dataSourceId=" + dataSourceId +
                ", sourceTableName='" + sourceTableName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
