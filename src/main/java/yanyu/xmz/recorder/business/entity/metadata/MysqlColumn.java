package yanyu.xmz.recorder.business.entity.metadata;

import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;
import yanyu.xmz.recorder.business.dao.annotation.TableField;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/8/10
 *
 * 查询表结构元数据 select * from INFORMATION_SCHEMA.COLUMNS
 */
public class MysqlColumn {

    @Id
    private Long id;

    @TableField(type = "varchar(512) NOT NULL DEFAULT ''")
    private String tableCatalog;
    @TableField(type = "varchar(64) NOT NULL DEFAULT ''")
    private String tableSchema;
    @TableField(type = "varchar(64) NOT NULL DEFAULT ''")
    private String tableName;
    @TableField(type = "varchar(64) NOT NULL DEFAULT ''")
    private String columnName;
    private Long ordinalPosition;
    @TableField(type = "longtext")
    private String columnDefault;
    @TableField(type = "varchar(3) NOT NULL DEFAULT ''")
    private String isNullable;
    @TableField(type = "varchar(64) NOT NULL DEFAULT ''")
    private String dataType;
    private Long characterMaximumLength;
    private Long characterOctetLength;
    private Long numericPrecision;
    private Long numericScale;
    private Long datetimePrecision;
    @TableField(type = "varchar(32) DEFAULT NULL")
    private String characterSetName;
    @TableField(type = "varchar(32) DEFAULT NULL")
    private String collationName;
    @TableField(type = "longtext NOT NULL")
    private String columnType;
    @TableField(type = "varchar(3) NOT NULL DEFAULT ''")
    private String columnKey;
    @TableField(type = "varchar(30) NOT NULL DEFAULT ''")
    private String extra;
    @TableField(type = "varchar(80) NOT NULL DEFAULT ''")
    private String privileges;
    @TableField(type = "varchar(1024) NOT NULL DEFAULT ''")
    private String columnComment;
    @TableField(type = "longtext NOT NULL")
    private String generationExpression;


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

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Long getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Long ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Long getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(Long characterMaximumLength) {
        this.characterMaximumLength = characterMaximumLength;
    }

    public Long getCharacterOctetLength() {
        return characterOctetLength;
    }

    public void setCharacterOctetLength(Long characterOctetLength) {
        this.characterOctetLength = characterOctetLength;
    }

    public Long getNumericPrecision() {
        return numericPrecision;
    }

    public void setNumericPrecision(Long numericPrecision) {
        this.numericPrecision = numericPrecision;
    }

    public Long getNumericScale() {
        return numericScale;
    }

    public void setNumericScale(Long numericScale) {
        this.numericScale = numericScale;
    }

    public Long getDatetimePrecision() {
        return datetimePrecision;
    }

    public void setDatetimePrecision(Long datetimePrecision) {
        this.datetimePrecision = datetimePrecision;
    }

    public String getCharacterSetName() {
        return characterSetName;
    }

    public void setCharacterSetName(String characterSetName) {
        this.characterSetName = characterSetName;
    }

    public String getCollationName() {
        return collationName;
    }

    public void setCollationName(String collationName) {
        this.collationName = collationName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getGenerationExpression() {
        return generationExpression;
    }

    public void setGenerationExpression(String generationExpression) {
        this.generationExpression = generationExpression;
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

    @Override
    public String toString() {
        return "MysqlColumn{" +
                "id=" + id +
                ", tableCatalog='" + tableCatalog + '\'' +
                ", tableSchema='" + tableSchema + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", ordinalPosition=" + ordinalPosition +
                ", columnDefault='" + columnDefault + '\'' +
                ", isNullable='" + isNullable + '\'' +
                ", dataType='" + dataType + '\'' +
                ", characterMaximumLength=" + characterMaximumLength +
                ", characterOctetLength=" + characterOctetLength +
                ", numericPrecision=" + numericPrecision +
                ", numericScale=" + numericScale +
                ", datetimePrecision=" + datetimePrecision +
                ", characterSetName='" + characterSetName + '\'' +
                ", collationName='" + collationName + '\'' +
                ", columnType='" + columnType + '\'' +
                ", columnKey='" + columnKey + '\'' +
                ", extra='" + extra + '\'' +
                ", privileges='" + privileges + '\'' +
                ", columnComment='" + columnComment + '\'' +
                ", generationExpression='" + generationExpression + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
