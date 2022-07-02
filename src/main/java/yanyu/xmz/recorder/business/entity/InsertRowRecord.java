package yanyu.xmz.recorder.business.entity;

import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;
import yanyu.xmz.recorder.business.dao.annotation.TableField;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 * 新增行记录表
 */
public class InsertRowRecord {

    @Id
    private Long id;

    private Long recordId;

    @TableField(value = "row", type = "text")
    private String row;

    @DateAuto
    private Date createTime;

    @DateAuto("update")
    private Date updateTime;

    public InsertRowRecord() {
    }

    public InsertRowRecord(Long recordId, String row) {
        this.recordId = recordId;
        this.row = row;
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

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
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
