package yanyu.xmz.recorder.entity;

import yanyu.xmz.recorder.dao.annotation.DateAuto;
import yanyu.xmz.recorder.dao.annotation.Id;
import yanyu.xmz.recorder.dao.annotation.TableField;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 * 删除行记录表
 */
public class DeleteRowRecord {

    @Id
    private Long id;

    private Long recordId;

    @TableField(value = "row", type = "text")
    private String row;

    @DateAuto
    private Date createTime;

    @DateAuto("update")
    private Date updateTime;

    public DeleteRowRecord() {
    }

    public DeleteRowRecord(Long recordId, String row) {
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
