package org.gitlab4j.api.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitlab4j.api.GitLabApiForm;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class NotificationService {

    public static final String NOTIFY_ONLY_BROKEN_PIPELINES_PROP = "notify_only_broken_pipelines";
    public static final String NOTIFY_ONLY_DEFAULT_BRANCH_PROP = "notify_only_default_branch";
    public static final String BRANCHES_TO_BE_NOTIFIED_PROP = "branches_to_be_notified";
    public static final String PUSH_CHANNEL_PROP = "push_channel";
    public static final String ISSUE_CHANNEL_PROP = "issue_channel";
    public static final String CONFIDENTIAL_ISSUE_CHANNEL_PROP = "confidential_issue_channel";
    public static final String MERGE_REQUEST_CHANNEL_PROP = "merge_request_channel";
    public static final String NOTE_CHANNEL_PROP = "note_channel";
    public static final String CONFIDENTIAL_NOTE_CHANNEL_PROP = "confidential_note_channel";
    public static final String TAG_PUSH_CHANNEL_PROP = "tag_push_channel";
    public static final String PIPELINE_CHANNEL_PROP = "pipeline_channel";
    public static final String WIKI_PAGE_CHANNEL_PROP = "wiki_page_channel";

    public static final String WEBHOOK_PROP = "webhook";
    public static final String USERNAME_PROP = "username";
    public static final String DESCRIPTION_PROP = "description";
    public static final String TITLE_PROP = "title";
    public static final String NEW_ISSUE_URL_PROP = "new_issue_url";
    public static final String ISSUES_URL_PROP = "issues_url";
    public static final String PROJECT_URL_PROP = "project_url";
    public static final String PUSH_EVENTS_PROP = "push_events";

    private Long id;
    private String title;
    private String slug;
    private Date createdAt;
    private Date updatedAt;
    private Boolean active;

    private Boolean commitEvents;
    private Boolean pushEvents;
    private Boolean issuesEvents;
    private Boolean confidentialIssuesEvents;
    private Boolean mergeRequestsEvents;
    private Boolean tagPushEvents;
    private Boolean noteEvents;
    private Boolean confidentialNoteEvents;
    private Boolean pipelineEvents;
    private Boolean wikiPageEvents;
    private Boolean jobEvents;

    private Map<String, Object> properties;

    public abstract GitLabApiForm servicePropertiesForm();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // *******************************************************************************
    // The following methods can be used to configure the notification service
    // *******************************************************************************

    public Boolean getCommitEvents() {
        return commitEvents;
    }

    public void setCommitEvents(Boolean commitEvents) {
        this.commitEvents = commitEvents;
    }

    protected <T> T withCommitEvents(Boolean commitEvents, T derivedInstance) {
        this.commitEvents = commitEvents;
        return (derivedInstance);
    }

    public Boolean getPushEvents() {
        return pushEvents;
    }

    public void setPushEvents(Boolean pushEvents) {
        this.pushEvents = pushEvents;
    }

    protected <T> T withPushEvents(Boolean pushEvents, T derivedInstance) {
        this.pushEvents = pushEvents;
        return (derivedInstance);
    }

    public Boolean getIssuesEvents() {
        return issuesEvents;
    }

    public void setIssuesEvents(Boolean issuesEvents) {
        this.issuesEvents = issuesEvents;
    }

    protected <T> T withIssuesEvents(Boolean issuesEvents, T derivedInstance) {
        this.issuesEvents = issuesEvents;
        return (derivedInstance);
    }

    public Boolean getConfidentialIssuesEvents() {
        return confidentialIssuesEvents;
    }

    public void setConfidentialIssuesEvents(Boolean confidentialIssuesEvents) {
        this.confidentialIssuesEvents = confidentialIssuesEvents;
    }

    protected <T> T withConfidentialIssuesEvents(Boolean confidentialIssuesEvents, T derivedInstance) {
        this.confidentialIssuesEvents = confidentialIssuesEvents;
        return (derivedInstance);
    }

    public Boolean getMergeRequestsEvents() {
        return mergeRequestsEvents;
    }

    public void setMergeRequestsEvents(Boolean mergeRequestsEvents) {
        this.mergeRequestsEvents = mergeRequestsEvents;
    }

    protected <T> T withMergeRequestsEvents(Boolean mergeRequestsEvents, T derivedInstance) {
        this.mergeRequestsEvents = mergeRequestsEvents;
        return (derivedInstance);
    }

    public Boolean getTagPushEvents() {
        return tagPushEvents;
    }

    public void setTagPushEvents(Boolean tagPushEvents) {
        this.tagPushEvents = tagPushEvents;
    }

    protected <T> T withTagPushEvents(Boolean tagPushEvents, T derivedInstance) {
        this.tagPushEvents = tagPushEvents;
        return (derivedInstance);
    }

    public Boolean getNoteEvents() {
        return noteEvents;
    }

    public void setNoteEvents(Boolean noteEvents) {
        this.noteEvents = noteEvents;
    }

    protected <T> T withNoteEvents(Boolean noteEvents, T derivedInstance) {
        this.noteEvents = noteEvents;
        return (derivedInstance);
    }

    public Boolean getConfidentialNoteEvents() {
        return confidentialNoteEvents;
    }

    public void setConfidentialNoteEvents(Boolean confidentialNoteEvents) {
        this.confidentialNoteEvents = confidentialNoteEvents;
    }

    protected <T> T withConfidentialNoteEvents(Boolean confidentialNoteEvents, T derivedInstance) {
        this.confidentialNoteEvents = confidentialNoteEvents;
        return (derivedInstance);
    }

    public Boolean getPipelineEvents() {
        return pipelineEvents;
    }

    public void setPipelineEvents(Boolean pipelineEvents) {
        this.pipelineEvents = pipelineEvents;
    }

    protected <T> T withPipelineEvents(Boolean pipelineEvents, T derivedInstance) {
        this.pipelineEvents = pipelineEvents;
        return (derivedInstance);
    }

    public Boolean getWikiPageEvents() {
        return wikiPageEvents;
    }

    public void setWikiPageEvents(Boolean wikiPageEvents) {
        this.wikiPageEvents = wikiPageEvents;
    }

    protected <T> T withWikiPageEvents(Boolean wikiPageEvents, T derivedInstance) {
        this.wikiPageEvents = wikiPageEvents;
        return (derivedInstance);
    }

    public Boolean getJobEvents() {
        return jobEvents;
    }

    public void setJobEvents(Boolean jobEvents) {
        this.jobEvents = jobEvents;
    }

    protected <T> T withJobEvents(Boolean jobEvents, T derivedInstance) {
        this.jobEvents = jobEvents;
        return (derivedInstance);
    }

    public Map<String, Object> getProperties() {
        return (properties);
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @JsonIgnore
    protected String getProperty(String prop) {
        return (getProperty(prop, ""));
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    protected <T> T getProperty(String prop, T defaultValue) {

        Object value = (properties != null ? properties.get(prop) : null);

        // HACK: Sometimes GitLab returns "0" or "1" for true/false
        if (value != null && Boolean.class.isInstance(defaultValue)) {
            if ("0".equals(value)) {
                return ((T) Boolean.FALSE);
            } else if ("1".equals(value)) {
                return ((T) Boolean.TRUE);
            }
        }

        return ((T) (value != null ? value : defaultValue));
    }

    protected void setProperty(String prop, Object value) {
        if (properties == null) {
            properties = new HashMap<>(16);
        }

        properties.put(prop, value);
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }

	public enum BranchesToBeNotified {
	    ALL, DEFAULT, PROTECTED, DEFAULT_AND_PROTECTED;
	    @Override
		public String toString() {
	        return (name().toLowerCase());
	    }
	}

}
