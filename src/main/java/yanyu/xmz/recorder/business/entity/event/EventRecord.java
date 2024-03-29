package yanyu.xmz.recorder.business.entity.event;

import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;
import yanyu.xmz.recorder.business.dao.annotation.TableField;
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
     * 语句开始执行的时间
     */
    private Date eventTimestamp;
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
    private String eventType;

    /**
     * 包含列
     */
    @TableField(type = "varchar(1024) DEFAULT NULL")
    private String includedColumns;
    /**
     * 更新之前的列
     */
    @TableField(type = "varchar(1024) DEFAULT NULL")
    private String includedColumnsBeforeUpdate;


    /**
     * 包含列名称
     */
    @TableField(type = "varchar(1024) DEFAULT NULL")
    private String includedColumnNames;
    /**
     * 更新之前的列名称
     */
    @TableField(type = "varchar(1024) DEFAULT NULL")
    private String columnNamesBeforeUpdate;

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
    /**
     * 业务字段
     */
    @DateAuto
    private Date createTime;
    /**
     * 业务字段
     */
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public Date getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Date eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }


    public String getIncludedColumnNames() {
        return includedColumnNames;
    }

    public void setIncludedColumnNames(String includedColumnNames) {
        this.includedColumnNames = includedColumnNames;
    }

    public String getColumnNamesBeforeUpdate() {
        return columnNamesBeforeUpdate;
    }

    public void setColumnNamesBeforeUpdate(String columnNamesBeforeUpdate) {
        this.columnNamesBeforeUpdate = columnNamesBeforeUpdate;
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
