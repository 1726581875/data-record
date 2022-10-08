package yanyu.xmz.recorder.business.model.dto;

/**
 * @author xiaomingzhang
 * @date 2022/9/14
 */
public class DataMigrationDTO {

    private String tenantId;

    private Long dataSourceId;

    private String tableName;

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


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "DaraMigrationDTO{" +
                "tenantId='" + tenantId + '\'' +
                ", dataSourceId=" + dataSourceId +
                ", tableName='" + tableName + '\'' +
                '}';
    }
}
