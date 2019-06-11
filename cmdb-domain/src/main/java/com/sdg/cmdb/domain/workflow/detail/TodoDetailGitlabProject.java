package com.sdg.cmdb.domain.workflow.detail;


import java.io.Serializable;


public class TodoDetailGitlabProject extends TodoDetailAbs implements Serializable {

    private static final long serialVersionUID = 3489911217137888945L;

    private int projectId;  // Gitlab ProjectId
    private String name;
    private String accessLevel;
    private int userId; // Gitlab UserId

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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
