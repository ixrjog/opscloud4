package org.gitlab4j.api.webhook;

import org.gitlab4j.api.models.User;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class JobEvent extends AbstractEvent {

    public static final String JOB_HOOK_X_GITLAB_EVENT = "Job Hook";
    public static final String OBJECT_KIND = "job";

    private String ref;
    private Boolean tag;
    private String beforeSha;
    private String sha;
    private Long jobId;
    private String jobName;
    private String jobStage;
    private String jobStatus;
    private Date jobStarted_at;
    private Date jobFinished_at;
    private Integer jobDuration;
    private Boolean jobAllowFailure;
    private String jobFailureReason;
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

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobStage() {
        return jobStage;
    }

    public void setJobStage(String jobStage) {
        this.jobStage = jobStage;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Date getJobStarted_at() {
        return jobStarted_at;
    }

    public void setJobStarted_at(Date jobStarted_at) {
        this.jobStarted_at = jobStarted_at;
    }

    public Date getJobFinished_at() {
        return jobFinished_at;
    }

    public void setJobFinished_at(Date jobFinished_at) {
        this.jobFinished_at = jobFinished_at;
    }

    public Integer getJobDuration() {
        return jobDuration;
    }

    public void setJobDuration(Integer jobDuration) {
        this.jobDuration = jobDuration;
    }

    public Boolean getJobAllowFailure() {
        return jobAllowFailure;
    }

    public void setJobAllowFailure(Boolean jobAllowFailure) {
        this.jobAllowFailure = jobAllowFailure;
    }

    public String getJobFailureReason() {
        return jobFailureReason;
    }

    public void setJobFailureReason(String jobFailureReason) {
        this.jobFailureReason = jobFailureReason;
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
