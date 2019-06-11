package com.sdg.cmdb.domain.workflow.detail;


import java.io.Serializable;

public class TodoDetailGitlabGroup extends TodoDetailAbs implements Serializable {
    private static final long serialVersionUID = 5953927601407707071L;

    private int groupId;  // Gitlab GroupId
    private String name;
    private String accessLevel;
    private int userId; // Gitlab UserId

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
