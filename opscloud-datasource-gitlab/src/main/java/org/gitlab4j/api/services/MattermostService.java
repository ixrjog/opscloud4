package org.gitlab4j.api.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitlab4j.api.GitLabApiForm;

public class MattermostService extends NotificationService {

    private String defaultChannel;

    /**
     * Get the form data for this service based on it's properties.
     *
     * @return the form data for this service based on it's properties
     */
    @Override
    public GitLabApiForm servicePropertiesForm() {
        GitLabApiForm formData = new GitLabApiForm()
            .withParam("webhook", getWebhook(), true)
            .withParam("username", getUsername())
            .withParam("channel", getDefaultChannel())
            .withParam("notify_only_broken_pipelines", getNotifyOnlyBrokenPipelines())
            .withParam("notify_only_default_branch", getNotifyOnlyDefaultBranch())
            .withParam("push_events", getPushEvents())
            .withParam("issues_events", getIssuesEvents())
            .withParam("confidential_issues_events", getConfidentialIssuesEvents())
            .withParam("merge_requests_events", getMergeRequestsEvents())
            .withParam("tag_push_events", getTagPushEvents())
            .withParam("note_events", getNoteEvents())
            .withParam("confidential_note_events", getConfidentialNoteEvents())
            .withParam("pipeline_events", getPipelineEvents())
            .withParam("wiki_page_events", getWikiPageEvents())
            .withParam("push_channel", getPushChannel())
            .withParam("issue_channel", getIssueChannel())
            .withParam("confidential_issue_channel", getConfidentialIssueChannel())
            .withParam("merge_request_channel", getMergeRequestChannel())
            .withParam("note_channel", getNoteChannel())
            .withParam("confidential_note_channel", getConfidentialNoteChannel())
            .withParam("tag_push_channel", getTagPushChannel())
            .withParam("pipeline_channel", getPipelineChannel())
            .withParam("wiki_page_channel", getWikiPageChannel());
        return formData;
    }

    public MattermostService withPushEvents(Boolean pushEvents) {
        return withPushEvents(pushEvents, this);
    }

    public MattermostService withIssuesEvents(Boolean issuesEvents) {
        return withIssuesEvents(issuesEvents, this);
    }

    public MattermostService withConfidentialIssuesEvents(Boolean confidentialIssuesEvents) {
        return withConfidentialIssuesEvents(confidentialIssuesEvents, this);
    }

    public MattermostService withMergeRequestsEvents(Boolean mergeRequestsEvents) {
        return withMergeRequestsEvents(mergeRequestsEvents, this);
    }

    public MattermostService withTagPushEvents(Boolean tagPushEvents) {
        return withTagPushEvents(tagPushEvents, this);
    }

    public MattermostService withNoteEvents(Boolean noteEvents) {
        return withNoteEvents(noteEvents, this);
    }

    public MattermostService withConfidentialNoteEvents(Boolean confidentialNoteEvents) {
        return withConfidentialNoteEvents(confidentialNoteEvents, this);
    }

    public MattermostService withPipelineEvents(Boolean pipelineEvents) {
        return withPipelineEvents(pipelineEvents, this);
    }

    public MattermostService withWikiPageEvents(Boolean wikiPageEvents) {
        return withWikiPageEvents(wikiPageEvents, this);
    }

    public MattermostService withJobEvents(Boolean jobEvents) {
        return withPipelineEvents(jobEvents, this);
    }

    @JsonIgnore
    public String getWebhook() {
        return ((String) getProperty(WEBHOOK_PROP));
    }

    public void setWebhook(String webhook) {
        setProperty(WEBHOOK_PROP, webhook);
    }

    public MattermostService withWebhook(String webhook) {
        setWebhook(webhook);
        return (this);
    }

    @JsonIgnore
    public String getUsername() {
        return ((String) getProperty(USERNAME_PROP));
    }

    public void setUsername(String username) {
        setProperty(USERNAME_PROP, username);
    }

    public MattermostService withUsername(String username) {
        setUsername(username);
        return (this);
    }

    @JsonIgnore
    public String getDefaultChannel() {
        return (defaultChannel);
    }

    public void setDefaultChannel(String defaultChannel) {
        this.defaultChannel = defaultChannel;
    }

    public MattermostService withDefaultChannelk(String defaultChannel) {
        this.defaultChannel = defaultChannel;
        return (this);
    }

    @JsonIgnore
    public Boolean getNotifyOnlyBrokenPipelines() {
        return ((Boolean) getProperty(NOTIFY_ONLY_BROKEN_PIPELINES_PROP, Boolean.FALSE));
    }

    public void setNotifyOnlyBrokenPipelines(Boolean notifyOnlyBrokenPipelines) {
        setProperty(NOTIFY_ONLY_BROKEN_PIPELINES_PROP, notifyOnlyBrokenPipelines);
    }

    public MattermostService withNotifyOnlyBrokenPipelines(Boolean notifyOnlyBrokenPipelines) {
        setNotifyOnlyBrokenPipelines(notifyOnlyBrokenPipelines);
        return (this);
    }

    @JsonIgnore
    public Boolean getNotifyOnlyDefaultBranch() {
        return ((Boolean) getProperty(NOTIFY_ONLY_DEFAULT_BRANCH_PROP, Boolean.FALSE));
    }

    public void setNotifyOnlyDefaultBranch(Boolean notifyOnlyDefaultBranch) {
        setProperty(NOTIFY_ONLY_DEFAULT_BRANCH_PROP, notifyOnlyDefaultBranch);
    }

    public MattermostService withNotifyOnlyDefaultBranch(Boolean notifyOnlyDefaultBranch) {
        setNotifyOnlyDefaultBranch(notifyOnlyDefaultBranch);
        return (this);
    }

    @JsonIgnore
    public String getPushChannel() {
        return ((String) getProperty(PUSH_CHANNEL_PROP));
    }

    public void setPushChannel(String pushChannel) {
        setProperty(PUSH_CHANNEL_PROP, pushChannel);
    }

    public MattermostService withPushChannel(String pushChannel) {
        setPushChannel(pushChannel);
        return (this);
    }

    @JsonIgnore
    public String getIssueChannel() {
        return ((String) getProperty(ISSUE_CHANNEL_PROP));
    }

    public void setIssueChannel(String issueChannel) {
        setProperty(ISSUE_CHANNEL_PROP, issueChannel);
    }

    public MattermostService withIssueChannel(String issueChannel) {
        setIssueChannel(issueChannel);
        return (this);
    }

    @JsonIgnore
    public String getConfidentialIssueChannel() {
        return ((String) getProperty(CONFIDENTIAL_ISSUE_CHANNEL_PROP));
    }

    public void setConfidentialIssueChannel(String confidentialIssueChannel) {
        setProperty(CONFIDENTIAL_ISSUE_CHANNEL_PROP, confidentialIssueChannel);
    }

    public MattermostService withConfidentialIssueChannel(String confidentialIssueChannel) {
        setConfidentialIssueChannel(confidentialIssueChannel);
        return (this);
    }

    @JsonIgnore
    public String getMergeRequestChannel() {
        return ((String) getProperty(MERGE_REQUEST_CHANNEL_PROP));
    }

    public void setMergeRequestChannel(String mergeRequestChannel) {
        setProperty(MERGE_REQUEST_CHANNEL_PROP, mergeRequestChannel);
    }

    public MattermostService withMergeRequestChannel(String mergeRequestChannel) {
        setMergeRequestChannel(mergeRequestChannel);
        return (this);
    }

    @JsonIgnore
    public String getNoteChannel() {
        return ((String) getProperty(NOTE_CHANNEL_PROP));
    }

    public void setNoteChannel(String noteChannel) {
        setProperty(NOTE_CHANNEL_PROP, noteChannel);
    }

    public MattermostService withNoteChannel(String noteChannel) {
        setNoteChannel(noteChannel);
        return (this);
    }

    @JsonIgnore
    public String getConfidentialNoteChannel() {
        return ((String) getProperty(CONFIDENTIAL_NOTE_CHANNEL_PROP));
    }

    public void setConfidentialNoteChannel(String noteChannel) {
        setProperty(NOTE_CHANNEL_PROP, noteChannel);
    }

    public MattermostService withConfidentialNoteChannel(String confidentialNoteChannel) {
        setConfidentialNoteChannel(confidentialNoteChannel);
        return (this);
    }

    @JsonIgnore
    public String getTagPushChannel() {
        return ((String) getProperty(TAG_PUSH_CHANNEL_PROP));
    }

    public void setTagPushChannel(String tagPushChannel) {
        setProperty(TAG_PUSH_CHANNEL_PROP, tagPushChannel);
    }

    public MattermostService withTagPushChannel(String tagPushChannel) {
        setTagPushChannel(tagPushChannel);
        return (this);
    }

    @JsonIgnore
    public String getPipelineChannel() {
        return ((String) getProperty(PIPELINE_CHANNEL_PROP));
    }

    public void setPipelineChannel(String pipelineChannel) {
        setProperty(PIPELINE_CHANNEL_PROP, pipelineChannel);
    }

    public MattermostService withPipelineChannel(String pipelineChannel) {
        setPipelineChannel(pipelineChannel);
        return (this);
    }

    @JsonIgnore
    public String getWikiPageChannel() {
        return ((String) getProperty(WIKI_PAGE_CHANNEL_PROP));
    }

    public void setWikiPageChannel(String wikiPageChannel) {
        setProperty(WIKI_PAGE_CHANNEL_PROP, wikiPageChannel);
    }

    public MattermostService withWikiPageChannel(String wikiPageChannel) {
        setWikiPageChannel(wikiPageChannel);
        return (this);
    }
}
