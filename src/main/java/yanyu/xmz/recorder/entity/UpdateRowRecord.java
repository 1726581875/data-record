package yanyu.xmz.recorder.entity;

import yanyu.xmz.recorder.dao.annotation.DateAuto;
import yanyu.xmz.recorder.dao.annotation.Id;
import yanyu.xmz.recorder.dao.annotation.TableField;
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

    @TableField(value = "before", type = "text")
    private String before;

    @TableField(value = "after", type = "text")
    private String after;

    @DateAuto
    private Date createTime;

    @DateAuto("update")
    private Date updateTime;


    public UpdateRowRecord(Long recordId, String before, String after) {
        this.recordId = recordId;
        this.before = before;
        this.after = after;
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

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
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
