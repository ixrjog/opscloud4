package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class SystemHook {

    private Long id;
    private String url;
    private Date createdAt;
    private Boolean pushEvents;
    private Boolean tagPushEvents;
    private Boolean enableSslVerification;
    private Boolean repositoryUpdateEvents;
    private Boolean mergeRequestsEvents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public Boolean getEnableSslVerification() {
        return enableSslVerification;
    }

    public void setEnableSslVerification(Boolean enableSslVerification) {
        this.enableSslVerification = enableSslVerification;
    }

    public void setRepositoryUpdateEvents(Boolean repositoryUpdateEvents) {
        this.repositoryUpdateEvents = repositoryUpdateEvents;
    }

    public Boolean getRepositoryUpdateEvents() {
        return repositoryUpdateEvents;
    }

    public void setMergeRequestsEvents(Boolean mergeRequestsEvents) {
        this.mergeRequestsEvents = mergeRequestsEvents;
    }

    public Boolean getMergeRequestsEvents() {
        return mergeRequestsEvents;
    }

    public SystemHook withId(Long id) {
        this.id = id;
        return (this);
    }

    public SystemHook withUrl(String url) {
        this.url = url;
        return (this);
    }

    public SystemHook withCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return (this);
    }

    public SystemHook withPushEvents(Boolean pushEvents) {
        this.pushEvents = pushEvents;
        return (this);
    }

    public SystemHook withTagPushEvents(Boolean tagPushEvents) {
        this.tagPushEvents = tagPushEvents;
        return (this);
    }

    public SystemHook withEnableSslVerification(Boolean enableSslVerification) {
        this.enableSslVerification = enableSslVerification;
        return (this);
    }

    public SystemHook withRepositoryUpdateEvents(Boolean repositoryUpdateEvents) {
        this.repositoryUpdateEvents = repositoryUpdateEvents;
        return (this);
    }

    public SystemHook withMergeRequestsEvents(Boolean mergeRequestsEvents) {
        this.mergeRequestsEvents = mergeRequestsEvents;
        return (this);
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
