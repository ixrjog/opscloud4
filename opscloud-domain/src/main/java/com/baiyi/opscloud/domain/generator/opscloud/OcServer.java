package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "oc_server")
public class OcServer implements Serializable {
    private static final long serialVersionUID = 2234702948079569429L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "server_group_id")
    private Integer serverGroupId;

    @Column(name = "login_type")
    private Integer loginType;

    @Column(name = "login_user")
    private String loginUser;

    @Column(name = "env_type")
    private Integer envType;

    @Column(name = "public_ip")
    private String publicIp;

    @Column(name = "private_ip")
    private String privateIp;

    @Column(name = "server_type")
    private Integer serverType;

    private String area;

    @Column(name = "serial_number")
    private Integer serialNumber;

    @Column(name = "monitor_status")
    private Integer monitorStatus;

    private String comment;

    @Column(name = "server_status")
    private Integer serverStatus;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;


    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return server_group_id
     */
    public Integer getServerGroupId() {
        return serverGroupId;
    }

    /**
     * @param serverGroupId
     */
    public void setServerGroupId(Integer serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    /**
     * @return login_type
     */
    public Integer getLoginType() {
        return loginType;
    }

    /**
     * @param loginType
     */
    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    /**
     * @return login_user
     */
    public String getLoginUser() {
        return loginUser;
    }

    /**
     * @param loginUser
     */
    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    /**
     * @return env_type
     */
    public Integer getEnvType() {
        return envType;
    }

    /**
     * @param envType
     */
    public void setEnvType(Integer envType) {
        this.envType = envType;
    }

    /**
     * @return public_ip
     */
    public String getPublicIp() {
        return publicIp;
    }

    /**
     * @param publicIp
     */
    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    /**
     * @return private_ip
     */
    public String getPrivateIp() {
        return privateIp;
    }

    /**
     * @param privateIp
     */
    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    /**
     * @return server_type
     */
    public Integer getServerType() {
        return serverType;
    }

    /**
     * @param serverType
     */
    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }

    /**
     * @return area
     */
    public String getArea() {
        return area;
    }

    /**
     * @param area
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * @return serial_number
     */
    public Integer getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber
     */
    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @return monitor_status
     */
    public Integer getMonitorStatus() {
        return monitorStatus;
    }

    /**
     * @param monitorStatus
     */
    public void setMonitorStatus(Integer monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public Integer getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(Integer serverStatus) {
        this.serverStatus = serverStatus;
    }

    /**
     * @return is_active
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}