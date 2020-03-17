package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "terminal_session")
public class TerminalSession {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String user;

    private String asset;

    @Column(name = "system_user")
    private String systemUser;

    @Column(name = "login_from")
    private String loginFrom;

    @Column(name = "is_finished")
    private Boolean isFinished;

    @Column(name = "has_replay")
    private Boolean hasReplay;

    @Column(name = "has_command")
    private Boolean hasCommand;

    @Column(name = "date_start")
    private Date dateStart;

    @Column(name = "date_end")
    private Date dateEnd;

    @Column(name = "terminal_id")
    private String terminalId;

    @Column(name = "remote_addr")
    private String remoteAddr;

    private String protocol;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "asset_id")
    private String assetId;

    @Column(name = "system_user_id")
    private String systemUserId;

    @Column(name = "user_id")
    private String userId;

    private String terminalName;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return asset
     */
    public String getAsset() {
        return asset;
    }

    /**
     * @param asset
     */
    public void setAsset(String asset) {
        this.asset = asset;
    }

    /**
     * @return system_user
     */
    public String getSystemUser() {
        return systemUser;
    }

    /**
     * @param systemUser
     */
    public void setSystemUser(String systemUser) {
        this.systemUser = systemUser;
    }

    /**
     * @return login_from
     */
    public String getLoginFrom() {
        return loginFrom;
    }

    /**
     * @param loginFrom
     */
    public void setLoginFrom(String loginFrom) {
        this.loginFrom = loginFrom;
    }

    /**
     * @return is_finished
     */
    public Boolean getIsFinished() {
        return isFinished;
    }

    /**
     * @param isFinished
     */
    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    /**
     * @return has_replay
     */
    public Boolean getHasReplay() {
        return hasReplay;
    }

    /**
     * @param hasReplay
     */
    public void setHasReplay(Boolean hasReplay) {
        this.hasReplay = hasReplay;
    }

    /**
     * @return has_command
     */
    public Boolean getHasCommand() {
        return hasCommand;
    }

    /**
     * @param hasCommand
     */
    public void setHasCommand(Boolean hasCommand) {
        this.hasCommand = hasCommand;
    }

    /**
     * @return date_start
     */
    public Date getDateStart() {
        return dateStart;
    }

    /**
     * @param dateStart
     */
    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    /**
     * @return date_end
     */
    public Date getDateEnd() {
        return dateEnd;
    }

    /**
     * @param dateEnd
     */
    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    /**
     * @return terminal_id
     */
    public String getTerminalId() {
        return terminalId;
    }

    /**
     * @param terminalId
     */
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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
     * @return protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return org_id
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * @param orgId
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * @return asset_id
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     * @param assetId
     */
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    /**
     * @return system_user_id
     */
    public String getSystemUserId() {
        return systemUserId;
    }

    /**
     * @param systemUserId
     */
    public void setSystemUserId(String systemUserId) {
        this.systemUserId = systemUserId;
    }

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }
}