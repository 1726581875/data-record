package yanyu.xmz.recorder.business.entity;

import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;
import yanyu.xmz.recorder.business.enums.StateEnum;
import yanyu.xmz.recorder.business.enums.StepEnum;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/6/13
 * 事件记录表
 */
public class EventRecord {

    @Id
    private Long id;
    /**
     * binlog文件名
     */
    private String binLogFileName;
    /**
     * binlog开始位置
     */
    private Long pos;
    /**
     * binlog结束
     */
    private Long endLogPos;
    /**
     * 数据库名
     */
    private String databaseName;

    private String tableId;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 操作类型
     * @see  com.github.shyiko.mysql.binlog.event.EventType
     */
    private String operationType;

    /**
     * 包含列
     */
    private String includedColumns;
    /**
     * 更新之后的列
     */
    private String includedColumnsBeforeUpdate;

    /**
     * 业务步骤
     * @see StepEnum
     */
    private String step;

    /**
     * 业务状态
     * @see StateEnum
     */
    private String state;

    @DateAuto
    private Date createTime;

    @DateAuto("update")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBinLogFileName() {
        return binLogFileName;
    }

    public void setBinLogFileName(String binLogFileName) {
        this.binLogFileName = binLogFileName;
    }

    public Long getPos() {
        return pos;
    }

    public void setPos(Long pos) {
        this.pos = pos;
    }

    public Long getEndLogPos() {
        return endLogPos;
    }

    public void setEndLogPos(Long endLogPos) {
        this.endLogPos = endLogPos;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getIncludedColumns() {
        return includedColumns;
    }

    public void setIncludedColumns(String includedColumns) {
        this.includedColumns = includedColumns;
    }

    public String getIncludedColumnsBeforeUpdate() {
        return includedColumnsBeforeUpdate;
    }

    public void setIncludedColumnsBeforeUpdate(String includedColumnsBeforeUpdate) {
        this.includedColumnsBeforeUpdate = includedColumnsBeforeUpdate;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
