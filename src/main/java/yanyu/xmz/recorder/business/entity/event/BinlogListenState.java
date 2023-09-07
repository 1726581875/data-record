package yanyu.xmz.recorder.business.entity.event;
import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2023/9/7
 */
public class BinlogListenState {

    public static final String STATE_LISTENING = "1";
    public static final String STATE_EXCEPTION = "2";
    public static final String STATE_CANCEL = "3";
    public static final String STATE_SERVER_RESTART = "4";

    @Id
    private Long id;

    private String tenantId;

    private Long dataSourceId;

    @DateAuto
    private Date createTime;

    @DateAuto("update")
    private Date updateTime;

    /**
     * 业务状态
     * 1:监听中
     * 2:发生异常，取消监听
     * 3:手动，取消监听
     * 4:服务重启
     */
    private String state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
