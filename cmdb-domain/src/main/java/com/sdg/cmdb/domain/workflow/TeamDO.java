package com.sdg.cmdb.domain.workflow;

import java.io.Serializable;

public class TeamDO implements Serializable {
    private static final long serialVersionUID = -3207846627940612212L;

    private long id;

    private String teamName;

    private String content;

    private int teamType;

    private long teamleaderUserId;

    private String teamleaderUsername;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTeamType() {
        return teamType;
    }

    public void setTeamType(int teamType) {
        this.teamType = teamType;
    }

    public long getTeamleaderUserId() {
        return teamleaderUserId;
    }

    public void setTeamleaderUserId(long teamleaderUserId) {
        this.teamleaderUserId = teamleaderUserId;
    }

    public String getTeamleaderUsername() {
        return teamleaderUsername;
    }

    public void setTeamleaderUsername(String teamleaderUsername) {
        this.teamleaderUsername = teamleaderUsername;
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
