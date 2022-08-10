package yanyu.xmz.recorder.business.entity;

import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;
import yanyu.xmz.recorder.business.dao.annotation.TableField;
import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 * 更新行记录表
 */
public class UpdateRowRecord {

    @Id
    private Long id;

    private Long recordId;

    @TableField(value = "before_row", type = "text")
    private String beforeRow;

    @TableField(value = "after_row", type = "text")
    private String afterRow;

    @DateAuto
    private Date createTime;

    @DateAuto("update")
    private Date updateTime;


    public UpdateRowRecord(Long recordId, String before, String after) {
        this.recordId = recordId;
        this.beforeRow = before;
        this.afterRow = after;
    }

    public UpdateRowRecord() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getBeforeRow() {
        return beforeRow;
    }

    public void setBeforeRow(String beforeRow) {
        this.beforeRow = beforeRow;
    }

    public String getAfterRow() {
        return afterRow;
    }

    public void setAfterRow(String afterRow) {
        this.afterRow = afterRow;
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
