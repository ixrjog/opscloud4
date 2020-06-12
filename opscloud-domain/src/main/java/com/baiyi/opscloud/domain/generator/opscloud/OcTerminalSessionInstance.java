package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_terminal_session_instance")
public class OcTerminalSessionInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 会话uuid
     */
    @Column(name = "session_id")
    private String sessionId;

    /**
     * 实例id
     */
    @Column(name = "instance_id")
    private String instanceId;

    /**
     * 会话复制实例id
     */
    @Column(name = "duplicate_instance_id")
    private String duplicateInstanceId;

    /**
     * 系统账户
     */
    @Column(name = "system_user")
    private String systemUser;

    /**
     * 主机ip
     */
    @Column(name = "host_ip")
    private String hostIp;

    @Column(name = "output_size")
    private Long outputSize;

    /**
     * 是否关闭
     */
    @Column(name = "is_closed")
    private Boolean isClosed;

    /**
     * 打开时间
     */
    @Column(name = "open_time")
    private Date openTime;

    /**
     * 关闭时间
     */
    @Column(name = "close_time")
    private Date closeTime;

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
     * 获取会话uuid
     *
     * @return session_id - 会话uuid
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * 设置会话uuid
     *
     * @param sessionId 会话uuid
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * 获取实例id
     *
     * @return instance_id - 实例id
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * 设置实例id
     *
     * @param instanceId 实例id
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * 获取会话复制实例id
     *
     * @return duplicate_instance_id - 会话复制实例id
     */
    public String getDuplicateInstanceId() {
        return duplicateInstanceId;
    }

    /**
     * 设置会话复制实例id
     *
     * @param duplicateInstanceId 会话复制实例id
     */
    public void setDuplicateInstanceId(String duplicateInstanceId) {
        this.duplicateInstanceId = duplicateInstanceId;
    }

    /**
     * 获取系统账户
     *
     * @return system_user - 系统账户
     */
    public String getSystemUser() {
        return systemUser;
    }

    /**
     * 设置系统账户
     *
     * @param systemUser 系统账户
     */
    public void setSystemUser(String systemUser) {
        this.systemUser = systemUser;
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

    public Long getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(Long outputSize) {
        this.outputSize = outputSize;
    }

    /**
     * 获取是否关闭
     *
     * @return is_closed - 是否关闭
     */
    public Boolean getIsClosed() {
        return isClosed;
    }

    /**
     * 设置是否关闭
     *
     * @param isClosed 是否关闭
     */
    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    /**
     * 获取打开时间
     *
     * @return open_time - 打开时间
     */
    public Date getOpenTime() {
        return openTime;
    }

    /**
     * 设置打开时间
     *
     * @param openTime 打开时间
     */
    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    /**
     * 获取关闭时间
     *
     * @return close_time - 关闭时间
     */
    public Date getCloseTime() {
        return closeTime;
    }

    /**
     * 设置关闭时间
     *
     * @param closeTime 关闭时间
     */
    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
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