package yanyu.xmz.recorder.business.model.dto;

/**
 * @author xiaomingzhang
 * @date 2022/10/8
 */
public class DataExportDTO extends TenantDataSourceDTO {

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "DataExportDTO{" +
                "tableName='" + tableName + '\'' +
                '}';
    }
}
