package org.gitlab4j.api.webhook;

import org.gitlab4j.api.models.User;
import org.gitlab4j.api.utils.JacksonJson;

public class DeploymentEvent extends AbstractEvent {

    public static final String X_GITLAB_EVENT = "Deployment Hook";
    public static final String OBJECT_KIND = "deployment";

    private String status;
    private String statusChangedAt;
    private Long deployableId;
    private String deployableUrl;
    private String environment;
    private EventProject project;
    private String shortSha;
    private User user;
    private String userUrl;
    private String commitUrl;
    private String commitTitle;

    @Override
	public String getObjectKind() {
        return (OBJECT_KIND);
    }

    public void setObjectKind(String objectKind) {
        if (!OBJECT_KIND.equals(objectKind))
            throw new RuntimeException("Invalid object_kind (" + objectKind + "), must be '" + OBJECT_KIND + "'");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusChangedAt() {
        return statusChangedAt;
    }

    public void setStatusChangedAt(String statusChangedAt) {
        this.statusChangedAt = statusChangedAt;
    }

    public Long getDeployableId() {
        return deployableId;
    }

    public void setDeployableId(Long deployableId) {
        this.deployableId = deployableId;
    }

    public String getDeployableUrl() {
        return deployableUrl;
    }

    public void setDeployableUrl(String deployableUrl) {
        this.deployableUrl = deployableUrl;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public EventProject getProject() {
        return project;
    }

    public void setProject(EventProject project) {
        this.project = project;
    }

    public String getShortSha() {
        return shortSha;
    }

    public void setShortSha(String shortSha) {
        this.shortSha = shortSha;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getCommitUrl() {
        return commitUrl;
    }

    public void setCommitUrl(String commitUrl) {
        this.commitUrl = commitUrl;
    }

    public String getCommitTitle() {
        return commitTitle;
    }

    public void setCommitTitle(String commitTitle) {
        this.commitTitle = commitTitle;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
