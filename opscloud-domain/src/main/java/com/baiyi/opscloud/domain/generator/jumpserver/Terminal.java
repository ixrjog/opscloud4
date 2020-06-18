package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;
import java.util.Date;

public class Terminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    @Column(name = "remote_addr")
    private String remoteAddr;

    @Column(name = "ssh_port")
    private Integer sshPort;

    @Column(name = "http_port")
    private Integer httpPort;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "command_storage")
    private String commandStorage;

    @Column(name = "replay_storage")
    private String replayStorage;

    private String comment;

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
     * @return ssh_port
     */
    public Integer getSshPort() {
        return sshPort;
    }

    /**
     * @param sshPort
     */
    public void setSshPort(Integer sshPort) {
        this.sshPort = sshPort;
    }

    /**
     * @return http_port
     */
    public Integer getHttpPort() {
        return httpPort;
    }

    /**
     * @param httpPort
     */
    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }

    /**
     * @return is_accepted
     */
    public Boolean getIsAccepted() {
        return isAccepted;
    }

    /**
     * @param isAccepted
     */
    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    /**
     * @return is_deleted
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * @return date_created
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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

    /**
     * @return command_storage
     */
    public String getCommandStorage() {
        return commandStorage;
    }

    /**
     * @param commandStorage
     */
    public void setCommandStorage(String commandStorage) {
        this.commandStorage = commandStorage;
    }

    /**
     * @return replay_storage
     */
    public String getReplayStorage() {
        return replayStorage;
    }

    /**
     * @param replayStorage
     */
    public void setReplayStorage(String replayStorage) {
        this.replayStorage = replayStorage;
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