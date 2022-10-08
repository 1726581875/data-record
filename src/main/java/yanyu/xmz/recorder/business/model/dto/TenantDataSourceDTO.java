package yanyu.xmz.recorder.business.model.dto;

/**
 * @author xiaomingzhang
 * @date 2022/10/8
 */
public class TenantDataSourceDTO {

    private String tenantId;

    private String dataSourceId;

    public TenantDataSourceDTO() {
    }

    public TenantDataSourceDTO(String tenantId, String dataSourceId) {
        this.tenantId = tenantId;
        this.dataSourceId = dataSourceId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    @Override
    public String toString() {
        return "TenantDataSourceDTO{" +
                "tenantId='" + tenantId + '\'' +
                ", dataSourceId='" + dataSourceId + '\'' +
                '}';
    }
}
