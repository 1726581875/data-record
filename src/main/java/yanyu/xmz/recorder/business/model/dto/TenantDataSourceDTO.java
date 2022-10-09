package yanyu.xmz.recorder.business.model.dto;

/**
 * @author xiaomingzhang
 * @date 2022/10/8
 */
public class TenantDataSourceDTO {

    private String tenantId;

    private Long dataSourceId;

    public TenantDataSourceDTO() {
    }

    public TenantDataSourceDTO(String tenantId, Long dataSourceId) {
        this.tenantId = tenantId;
        this.dataSourceId = dataSourceId;
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

    @Override
    public String toString() {
        return "TenantDataSourceDTO{" +
                "tenantId='" + tenantId + '\'' +
                ", dataSourceId='" + dataSourceId + '\'' +
                '}';
    }
}
