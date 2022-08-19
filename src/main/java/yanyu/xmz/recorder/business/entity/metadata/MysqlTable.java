package yanyu.xmz.recorder.business.entity.metadata;

import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;
import yanyu.xmz.recorder.business.dao.annotation.TableField;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/8/17
 * SELECT * FROM information_schema.TABLES WHERE table_schema= 'mooc';
 */
public class MysqlTable {

    @Id
    private Long id;

    @TableField(type = "varchar(512) NOT NULL DEFAULT ''")
    private String tableCatalog;
    @TableField(type = "varchar(64) NOT NULL DEFAULT ''")
    private String tableSchema;
    @TableField(type = "varchar(64) NOT NULL DEFAULT ''")
    private String tableName;
    @TableField(type = "varchar(64) NOT NULL DEFAULT ''")
    private String tableType;
    @TableField(type = "varchar(64) DEFAULT NULL")
    private String engine;
    private Long version;
    @TableField(type = "varchar(10) DEFAULT NULL")
    private String rowFormat;
    private Long tableRows;
    private Long avgRowLength;
    private Long dataLength;
    private Long maxDataLength;
    private Long indexLength;
    private Long dataFree;
    private Long autoIncrement;
    private Date createTime;
    private Date updateTime;
    private Date checkTime;
    @TableField(type = "varchar(32) DEFAULT NULL")
    private String tableCollation;
    private Long checksum;
    @TableField(type = "varchar(255) DEFAULT NULL")
    private String createOptions;
    @TableField(type = "varchar(2048) NOT NULL DEFAULT ''")
    private String tableComment;

    @DateAuto
    private Date dataCreateTime;

    @DateAuto("update")
    private Date dataUpdateTime;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getRowFormat() {
        return rowFormat;
    }

    public void setRowFormat(String rowFormat) {
        this.rowFormat = rowFormat;
    }

    public Long getTableRows() {
        return tableRows;
    }

    public void setTableRows(Long tableRows) {
        this.tableRows = tableRows;
    }

    public Long getAvgRowLength() {
        return avgRowLength;
    }

    public void setAvgRowLength(Long avgRowLength) {
        this.avgRowLength = avgRowLength;
    }

    public Long getDataLength() {
        return dataLength;
    }

    public void setDataLength(Long dataLength) {
        this.dataLength = dataLength;
    }

    public Long getMaxDataLength() {
        return maxDataLength;
    }

    public void setMaxDataLength(Long maxDataLength) {
        this.maxDataLength = maxDataLength;
    }

    public Long getIndexLength() {
        return indexLength;
    }

    public void setIndexLength(Long indexLength) {
        this.indexLength = indexLength;
    }

    public Long getDataFree() {
        return dataFree;
    }

    public void setDataFree(Long dataFree) {
        this.dataFree = dataFree;
    }

    public Long getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Long autoIncrement) {
        this.autoIncrement = autoIncrement;
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

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getTableCollation() {
        return tableCollation;
    }

    public void setTableCollation(String tableCollation) {
        this.tableCollation = tableCollation;
    }

    public Long getChecksum() {
        return checksum;
    }

    public void setChecksum(Long checksum) {
        this.checksum = checksum;
    }

    public String getCreateOptions() {
        return createOptions;
    }

    public void setCreateOptions(String createOptions) {
        this.createOptions = createOptions;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public Date getDataCreateTime() {
        return dataCreateTime;
    }

    public void setDataCreateTime(Date dataCreateTime) {
        this.dataCreateTime = dataCreateTime;
    }

    public Date getDataUpdateTime() {
        return dataUpdateTime;
    }

    public void setDataUpdateTime(Date dataUpdateTime) {
        this.dataUpdateTime = dataUpdateTime;
    }
}
