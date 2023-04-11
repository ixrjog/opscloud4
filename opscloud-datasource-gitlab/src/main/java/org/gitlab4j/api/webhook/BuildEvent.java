package org.gitlab4j.api.webhook;

import org.gitlab4j.api.models.User;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

/**
 * The documentation at: <a href="https://docs.gitlab.com/ee/user/project/integrations/webhooks.html#job-events">
 * Job Events</a> is incorrect, this class represents the actual content of the Job Hook event.
 */
public class BuildEvent extends AbstractEvent {

    public static final String JOB_HOOK_X_GITLAB_EVENT = "Job Hook";
    public static final String OBJECT_KIND = "build";

    private String ref;
    private Boolean tag;
    private String beforeSha;
    private String sha;
    private Long buildId;
    private String buildName;
    private String buildStage;
    private String buildStatus;
    private Date buildStarted_at;
    private Date buildFinished_at;
    private Float buildDuration;
    private Boolean buildAllowFailure;
    private String buildFailureReason;
    private Long projectId;
    private String projectName;
    private User user;
    private BuildCommit commit;
    private EventRepository repository;

    @Override
	public String getObjectKind() {
        return (OBJECT_KIND);
    }

    public void setObjectKind(String objectKind) {
        if (!OBJECT_KIND.equals(objectKind))
            throw new RuntimeException("Invalid object_kind (" + objectKind + "), must be '" + OBJECT_KIND + "'");
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Boolean getTag() {
        return tag;
    }

    public void setTag(Boolean tag) {
        this.tag = tag;
    }

    public String getBeforeSha() {
        return beforeSha;
    }

    public void setBeforeSha(String beforeSha) {
        this.beforeSha = beforeSha;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public Long getBuildId() {
        return buildId;
    }

    public void setBuildId(Long buildId) {
        this.buildId = buildId;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public String getBuildStage() {
        return buildStage;
    }

    public void setBuildStage(String buildStage) {
        this.buildStage = buildStage;
    }

    public String getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }

    public Date getBuildStarted_at() {
        return buildStarted_at;
    }

    public void setBuildStarted_at(Date buildStarted_at) {
        this.buildStarted_at = buildStarted_at;
    }

    public Date getBuildFinished_at() {
        return buildFinished_at;
    }

    public void setBuildFinished_at(Date buildFinished_at) {
        this.buildFinished_at = buildFinished_at;
    }

    public Float getBuildDuration() {
        return buildDuration;
    }

    public void setBuildDuration(Float buildDuration) {
        this.buildDuration = buildDuration;
    }

    public Boolean getBuildAllowFailure() {
        return buildAllowFailure;
    }

    public void setBuildAllowFailure(Boolean buildAllowFailure) {
        this.buildAllowFailure = buildAllowFailure;
    }

    public String getBuildFailureReason() {
        return buildFailureReason;
    }

    public void setBuildFailureReason(String buildFailureReason) {
        this.buildFailureReason = buildFailureReason;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BuildCommit getCommit() {
        return commit;
    }

    public void setCommit(BuildCommit commit) {
        this.commit = commit;
    }

    public EventRepository getRepository() {
        return repository;
    }

    public void setRepository(EventRepository repository) {
        this.repository = repository;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
