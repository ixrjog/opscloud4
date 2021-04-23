package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_instance")
public class OcInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例名
     */
    private String name;

    /**
     * 主机名
     */
    private String hostname;

    /**
     * 主机ip
     */
    @Column(name = "host_ip")
    private String hostIp;

    /**
     * 实例状态
     */
    @Column(name = "instance_status")
    private Integer instanceStatus;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time",insertable = false,updatable = false)
    private Date createTime;

    @Column(name = "update_time",insertable = false,updatable = false)
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
     * 获取实例名
     *
     * @return name - 实例名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置实例名
     *
     * @param name 实例名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取主机名
     *
     * @return hostname - 主机名
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * 设置主机名
     *
     * @param hostname 主机名
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * 获取主机ip
     *
     * @return host_ip - 主机ip
     */
    public String getHostIp() {
        return hostIp;
    }

    /**
     * 设置主机ip
     *
     * @param hostIp 主机ip
     */
    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    /**
     * 获取实例状态
     *
     * @return instance_status - 实例状态
     */
    public Integer getInstanceStatus() {
        return instanceStatus;
    }

    /**
     * 设置实例状态
     *
     * @param instanceStatus 实例状态
     */
    public void setInstanceStatus(Integer instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    /**
     * 获取有效
     *
     * @return is_active - 有效
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * 设置有效
     *
     * @param isActive 有效
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * 获取说明
     *
     * @return comment - 说明
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置说明
     *
     * @param comment 说明
     */
    public void setComment(String comment) {
        this.comment = comment;
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
}