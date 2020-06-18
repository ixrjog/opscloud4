package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_terminal_session")
public class OcTerminalSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 会话uuid
     */
    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "user_id")
    private Integer userId;

    private String username;

    @Column(name = "remote_addr")
    private String remoteAddr;

    /**
     * 是否关闭
     */
    @Column(name = "is_closed")
    private Boolean isClosed;

    /**
     * 关闭时间
     */
    @Column(name = "close_time")
    private Date closeTime;

    /**
     * 服务端主机名
     */
    @Column(name = "term_hostname")
    private String termHostname;

    /**
     * 服务端ip
     */
    @Column(name = "term_addr")
    private String termAddr;

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
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return remote_addr
     */
    public String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * @param remoteAddr
     */
    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
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
     * 获取服务端主机名
     *
     * @return term_hostname - 服务端主机名
     */
    public String getTermHostname() {
        return termHostname;
    }

    /**
     * 设置服务端主机名
     *
     * @param termHostname 服务端主机名
     */
    public void setTermHostname(String termHostname) {
        this.termHostname = termHostname;
    }

    /**
     * 获取服务端ip
     *
     * @return term_addr - 服务端ip
     */
    public String getTermAddr() {
        return termAddr;
    }

    /**
     * 设置服务端ip
     *
     * @param termAddr 服务端ip
     */
    public void setTermAddr(String termAddr) {
        this.termAddr = termAddr;
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