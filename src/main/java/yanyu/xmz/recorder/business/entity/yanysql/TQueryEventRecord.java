package yanyu.xmz.recorder.business.entity.yanysql;

import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;
import yanyu.xmz.recorder.business.dao.annotation.TableField;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/7/6
 */
public class TQueryEventRecord {

    @Id
    private String id;

    private String recordId;
    /**
     * _租户id_数据源id
     */
    private String tenantId;

    private Long threadId;
    private String databaseName;
    @TableField(value = "exec_sql", type = "text")
    private String execSql;
    private Integer errorCode;
    private Long executionTime;

    @DateAuto
    private Date createTime;

    @DateAuto("update")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getExecSql() {
        return execSql;
    }

    public void setExecSql(String execSql) {
        this.execSql = execSql;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
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
}
