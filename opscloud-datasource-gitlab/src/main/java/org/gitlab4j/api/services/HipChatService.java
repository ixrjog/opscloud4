package org.gitlab4j.api.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitlab4j.api.GitLabApiForm;

public class HipChatService extends NotificationService {

    public static final String TOKEN_PROP = "token";
    public static final String COLOR_PROP = "color";
    public static final String NOTIFY_PROP = "notify";
    public static final String ROOM_PROP = "room";
    public static final String API_VERSION_PROP = "api_version";
    public static final String SERVER_PROP = "server";
    public static final String NOTIFY_ONLY_BROKEN_PIPELINES_PROP = "notify_only_broken_pipelines";

    /**
     * Get the form data for this service based on it's properties.
     *
     * @return the form data for this service based on it's properties
     */
    @Override
    public GitLabApiForm servicePropertiesForm() {
        GitLabApiForm formData = new GitLabApiForm()
            .withParam("push_events", getPushEvents())
            .withParam("issues_events", getIssuesEvents())
            .withParam("confidential_issues_events", getConfidentialIssuesEvents())
            .withParam("merge_requests_events", getMergeRequestsEvents())
            .withParam("tag_push_events", getTagPushEvents())
            .withParam("note_events", getNoteEvents())
            .withParam("confidential_note_events", getConfidentialNoteEvents())
            .withParam("pipeline_events", getPipelineEvents())
            .withParam("token", getToken(), true)
            .withParam("color", getColor())
            .withParam("notify", getNotify())
            .withParam("room", getRoom())
            .withParam("api_version", getApiVersion())
            .withParam("server", getServer())
            .withParam("notify_only_broken_pipelines", getNotifyOnlyBrokenPipelines());
        return formData;
    }

    public HipChatService withPushEvents(Boolean pushEvents) {
        return withPushEvents(pushEvents, this);
    }

    public HipChatService withIssuesEvents(Boolean issuesEvents) {
        return withIssuesEvents(issuesEvents, this);
    }

    public HipChatService withConfidentialIssuesEvents(Boolean confidentialIssuesEvents) {
        return withConfidentialIssuesEvents(confidentialIssuesEvents, this);
    }

    public HipChatService withMergeRequestsEvents(Boolean mergeRequestsEvents) {
        return withMergeRequestsEvents(mergeRequestsEvents, this);
    }

    public HipChatService withTagPushEvents(Boolean tagPushEvents) {
        return withTagPushEvents(tagPushEvents, this);
    }

    public HipChatService withNoteEvents(Boolean noteEvents) {
        return withNoteEvents(noteEvents, this);
    }

    public HipChatService withConfidentialNoteEvents(Boolean confidentialNoteEvents) {
        return withConfidentialNoteEvents(confidentialNoteEvents, this);
    }

    public HipChatService withPipelineEvents(Boolean pipelineEvents) {
        return withPipelineEvents(pipelineEvents, this);
    }

    public HipChatService withWikiPageEvents(Boolean wikiPageEvents) {
        return withWikiPageEvents(wikiPageEvents, this);
    }

    public HipChatService withJobEvents(Boolean jobEvents) {
        return withPipelineEvents(jobEvents, this);
    }

    public String getToken() {
        return ((String) getProperty(TOKEN_PROP));
    }

    public void setToken(String token) {
        setProperty(TOKEN_PROP, token);
    }

    public HipChatService withToken(String token) {
        setToken(token);
        return (this);
    }

    public String getColor() {
        return ((String) getProperty(COLOR_PROP));
    }

    public void setColor(String color) {
        setProperty(COLOR_PROP, color);
    }

    public HipChatService withColor(String color) {
        setColor(color);
        return (this);
    }

    public Boolean getNotify() {
        return (getProperty(NOTIFY_PROP, (Boolean)null));
    }

    public void setNotify(Boolean notify) {
        setProperty(NOTIFY_PROP, notify);
    }

    public HipChatService withNotify(Boolean notify) {
        setNotify(notify);
        return (this);
    }

    public String getRoom() {
        return ((String) getProperty(ROOM_PROP));
    }

    public void setRoom(String room) {
        setProperty(ROOM_PROP, room);
    }

    public HipChatService withRoom(String room) {
        setRoom(room);
        return (this);
    }

    public String getApiVersion() {
        return ((String) getProperty(API_VERSION_PROP));
    }

    public void setApiVersion(String apiVersion) {
        setProperty(API_VERSION_PROP, apiVersion);
    }

    public HipChatService withApiVersion(String apiVersion) {
        setApiVersion(apiVersion);
        return (this);
    }

    public String getServer() {
        return ((String) getProperty(SERVER_PROP));
    }

    public void setServer(String server) {
        setProperty(SERVER_PROP, server);
    }

    public HipChatService withServer(String server) {
        setServer(server);
        return (this);
    }

    @JsonIgnore
    public Boolean getNotifyOnlyBrokenPipelines() {
        return ((Boolean) getProperty(NOTIFY_ONLY_BROKEN_PIPELINES_PROP, Boolean.FALSE));
    }

    public void setNotifyOnlyBrokenPipelines(Boolean notifyOnlyBrokenPipelines) {
        setProperty(NOTIFY_ONLY_BROKEN_PIPELINES_PROP, notifyOnlyBrokenPipelines);
    }

    public HipChatService withNotifyOnlyBrokenPipelines(Boolean notifyOnlyBrokenPipelines) {
        setNotifyOnlyBrokenPipelines(notifyOnlyBrokenPipelines);
        return (this);
    }
}
