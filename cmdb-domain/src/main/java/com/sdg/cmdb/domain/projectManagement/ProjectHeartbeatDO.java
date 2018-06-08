package com.sdg.cmdb.domain.projectManagement;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

public class ProjectHeartbeatDO implements Serializable {
    private static final long serialVersionUID = 1932673865764672342L;

    private long id;

    private long pmId;

    private long userId;

    private String username;

    /**
     * ProjectManagementDO.ProjectStatusEnum
     *
     */
    private int heartbeatType;

    private String gmtCreate;

    private String gmtModify;

    public ProjectHeartbeatDO(ProjectManagementDO pmDO, UserDO userDO ,int status){
        this.pmId = pmDO.getId();
        this.userId = userDO.getId();
        this.username = userDO.getUsername();
        this.heartbeatType = status;
    }

    public ProjectHeartbeatDO(){

    }


    @Override
    public String toString() {
        return "ProjectHeartbeatDO{" +
                "id=" + id +
                ", pmId=" + pmId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", heartbeatType=" + heartbeatType +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPmId() {
        return pmId;
    }

    public void setPmId(long pmId) {
        this.pmId = pmId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHeartbeatType() {
        return heartbeatType;
    }

    public void setHeartbeatType(int heartbeatType) {
        this.heartbeatType = heartbeatType;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }
}
