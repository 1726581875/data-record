package yanyu.xmz.recorder.business.entity.yanysql;

import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;
import yanyu.xmz.recorder.business.dao.annotation.TableField;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/6/14
 * 删除行记录表
 */
public class TDeleteRowRecord {

    @Id
    private String id;

    private String recordId;
    /**
     * _租户id_数据源id
     */
    private String tenantId;

    @TableField(value = "row", type = "text")
    private String row;

    @DateAuto
    private Date createTime;

    @DateAuto("update")
    private Date updateTime;

    public TDeleteRowRecord() {
    }

    public TDeleteRowRecord(String recordId, String row) {
        this.recordId = recordId;
        this.row = row;
    }

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

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }
}
