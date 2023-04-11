package org.gitlab4j.api.systemhooks;

import org.gitlab4j.api.models.Visibility;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class TeamMemberSystemHookEvent extends AbstractSystemHookEvent {

    public static final String NEW_TEAM_MEMBER_EVENT = "user_add_to_team";
    public static final String TEAM_MEMBER_REMOVED_EVENT = "user_remove_from_team";

    private Date createdAt;
    private Date updatedAt;
    private String eventName;
    private String projectAccess;
    private String projectName;
    private String projectPath;
    private Long projectId;
    private String projectPathWithNamespace;
    private String userEmail;
    private String userName;
    private String userUsername;
    private Long userId;
    private Visibility projectVisibility;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
	public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getProjectAccess() {
        return projectAccess;
    }

    public void setProjectAccess(String projectAccess) {
        this.projectAccess = projectAccess;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectPathWithNamespace() {
        return projectPathWithNamespace;
    }

    public void setProjectPathWithNamespace(String projectPathWithNamespace) {
        this.projectPathWithNamespace = projectPathWithNamespace;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Visibility getProjectVisibility() {
        return projectVisibility;
    }

    public void setProjectVisibility(Visibility projectVisibility) {
        this.projectVisibility = projectVisibility;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
