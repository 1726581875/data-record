package yanyu.xmz.recorder.business.model.entity;

import yanyu.xmz.recorder.business.dao.util.ConnectUtil;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/9/13
 */
public class SysDataSource {

    private Long id;

    private String name;

    private String tenantId;

    private String hostname;

    private String serverPort;

    private String schemaName;

    private String username;

    private String password;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    public ConnectUtil.Config getConfig() {
        return new ConnectUtil.Config(hostname, Integer.valueOf(serverPort), schemaName, username, password);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        return "SysDataSource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", hostname='" + hostname + '\'' +
                ", serverPort='" + serverPort + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
