package com.sdg.cmdb.domain.projectManagement;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

public class ProjectHeartbeatVO implements Serializable {

    private static final long serialVersionUID = 3465075945371738810L;

    private UserDO userDO;

    private int heartbeatType;

    private String timeView;

    private String gmtCreate;

    private String gmtModify;

    public ProjectHeartbeatVO() {

    }

    public ProjectHeartbeatVO(ProjectHeartbeatDO projectHeartbeatDO, UserDO userDO) {
        this.heartbeatType = projectHeartbeatDO.getHeartbeatType();
        this.gmtCreate = projectHeartbeatDO.getGmtCreate();
        this.gmtModify = projectHeartbeatDO.getGmtModify();
        this.userDO = userDO;
    }

    @Override
    public String toString() {
        return "ProjectHeartbeatVO{" +
                userDO.toString() +
                ", heartbeatType=" + heartbeatType +
                ", timeView='" + timeView + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }

    public int getHeartbeatType() {
        return heartbeatType;
    }

    public void setHeartbeatType(int heartbeatType) {
        this.heartbeatType = heartbeatType;
    }

    public String getTimeView() {
        return timeView;
    }

    public void setTimeView(String timeView) {
        this.timeView = timeView;
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
