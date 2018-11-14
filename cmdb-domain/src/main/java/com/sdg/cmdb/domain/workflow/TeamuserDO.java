package com.sdg.cmdb.domain.workflow;

import java.io.Serializable;

public class TeamuserDO implements Serializable {
    private static final long serialVersionUID = 7220285415834859881L;

    private long id;

    private long teamId;

    private String username;

    private int teamRole;

    private long userId;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(int teamRole) {
        this.teamRole = teamRole;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
