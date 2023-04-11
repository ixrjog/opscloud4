
package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class ProjectHook {

    private Boolean buildEvents;
    private Date createdAt;
    private Boolean enableSslVerification;
    private Long id;
    private Boolean issuesEvents;
    private Boolean mergeRequestsEvents;
    private Boolean noteEvents;
    private Boolean jobEvents;
    private Boolean pipelineEvents;
    private Long projectId;
    private Boolean pushEvents;
    private Boolean tagPushEvents;
    private String url;
    private Boolean wikiPageEvents;
    private String token;

    private Boolean repositoryUpdateEvents;
    private Boolean confidentialIssuesEvents;
    private Boolean confidentialNoteEvents;
    private String pushEventsBranchFilter;

    private Boolean deploymentEvents;
    private Boolean releasesEvents;

    public Boolean getBuildEvents() {
        return buildEvents;
    }

    public void setBuildEvents(Boolean buildEvents) {
        this.buildEvents = buildEvents;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getEnableSslVerification() {
        return enableSslVerification;
    }

    public void setEnableSslVerification(Boolean enableSslVerification) {
        this.enableSslVerification = enableSslVerification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIssuesEvents() {
        return issuesEvents;
    }

    public void setIssuesEvents(Boolean issuesEvents) {
        this.issuesEvents = issuesEvents;
    }

    public Boolean getMergeRequestsEvents() {
        return mergeRequestsEvents;
    }

    public void setMergeRequestsEvents(Boolean mergeRequestsEvents) {
        this.mergeRequestsEvents = mergeRequestsEvents;
    }

    public Boolean getNoteEvents() {
        return noteEvents;
    }

    public void setNoteEvents(Boolean noteEvents) {
        this.noteEvents = noteEvents;
    }

    public Boolean getJobEvents() {
        return jobEvents;
    }

    public void setJobEvents(Boolean jobEvents) {
        this.jobEvents = jobEvents;
    }

    public Boolean getPipelineEvents() {
        return pipelineEvents;
    }

    public void setPipelineEvents(Boolean pipelineEvents) {
        this.pipelineEvents = pipelineEvents;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Boolean getPushEvents() {
        return pushEvents;
    }

    public void setPushEvents(Boolean pushEvents) {
        this.pushEvents = pushEvents;
    }

    public Boolean getTagPushEvents() {
        return tagPushEvents;
    }

    public void setTagPushEvents(Boolean tagPushEvents) {
        this.tagPushEvents = tagPushEvents;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getWikiPageEvents() {
        return wikiPageEvents;
    }

    public void setWikiPageEvents(Boolean wikiPageEvents) {
        this.wikiPageEvents = wikiPageEvents;
    }

    public Boolean getRepositoryUpdateEvents() {
        return repositoryUpdateEvents;
    }

    public void setRepositoryUpdateEvents(Boolean repositoryUpdateEvents) {
        this.repositoryUpdateEvents = repositoryUpdateEvents;
    }

    public Boolean getDeploymentEvents() {
        return deploymentEvents;
    }

    public void setDeploymentEvents(Boolean releasesEvents) {
        this.deploymentEvents = releasesEvents;
    }

    public Boolean getReleasesEvents() {
        return releasesEvents;
    }

    public void setReleasesEvents(Boolean releasesEvents) {
        this.releasesEvents = releasesEvents;
    }

    public Boolean getConfidentialIssuesEvents() {
        return confidentialIssuesEvents;
    }

    public void setConfidentialIssuesEvents(Boolean confidentialIssuesEvents) {
        this.confidentialIssuesEvents = confidentialIssuesEvents;
    }

    public Boolean getConfidentialNoteEvents() {
        return confidentialNoteEvents;
    }

    public void setConfidentialNoteEvents(Boolean confidentialNoteEvents) {
        this.confidentialNoteEvents = confidentialNoteEvents;
    }

    public String getPushEventsBranchFilter() {
        return pushEventsBranchFilter;
    }

    public void setPushEventsBranchFilter(String pushEventsBranchFilter) {
        this.pushEventsBranchFilter = pushEventsBranchFilter;
    }

    public ProjectHook withIssuesEvents(Boolean issuesEvents) {
        this.issuesEvents = issuesEvents;
        return (this);
    }

    public ProjectHook withMergeRequestsEvents(Boolean mergeRequestsEvents) {
        this.mergeRequestsEvents = mergeRequestsEvents;
        return (this);
    }

    public ProjectHook withNoteEvents(Boolean noteEvents) {
        this.noteEvents = noteEvents;
        return (this);
    }

    public ProjectHook withJobEvents(Boolean jobEvents) {
        this.jobEvents = jobEvents;
        return (this);
    }

    public ProjectHook withPipelineEvents(Boolean pipelineEvents) {
        this.pipelineEvents = pipelineEvents;
        return (this);
    }

    public ProjectHook withPushEvents(Boolean pushEvents) {
        this.pushEvents = pushEvents;
        return (this);
    }

    public ProjectHook withTagPushEvents(Boolean tagPushEvents) {
        this.tagPushEvents = tagPushEvents;
        return (this);
    }

    public ProjectHook withWikiPageEvents(Boolean wikiPageEvents) {
        this.wikiPageEvents = wikiPageEvents;
        return (this);
    }

    public ProjectHook withRepositoryUpdateEvents(Boolean repositoryUpdateEvents) {
        this.repositoryUpdateEvents = repositoryUpdateEvents;
        return (this);
    }

    public ProjectHook withConfidentialIssuesEvents(Boolean confidentialIssuesEvents) {
        this.confidentialIssuesEvents = confidentialIssuesEvents;
        return (this);
    }

    public ProjectHook withConfidentialNoteEvents(Boolean confidentialNoteEvents) {
        this.confidentialNoteEvents = confidentialNoteEvents;
        return (this);
    }

    public ProjectHook withPushEventsBranchFilter(String pushEventsBranchFilter) {
        this.pushEventsBranchFilter = pushEventsBranchFilter;
        return (this);
    }

    public ProjectHook withDeploymentEvents(Boolean deploymentEvents) {
        this.deploymentEvents = deploymentEvents;
        return (this);
    }

    public ProjectHook withReleasesEvents(Boolean releasesEvents) {
        this.releasesEvents = releasesEvents;
        return (this);
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
