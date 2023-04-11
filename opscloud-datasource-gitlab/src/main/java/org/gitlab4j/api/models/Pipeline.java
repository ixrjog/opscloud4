package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class Pipeline {

    private Long id;
    private PipelineStatus status;
    private String ref;
    private String sha;
    private String beforeSha;
    private Boolean tag;
    private String yamlErrors;
    private User user;
    private Date createdAt;
    private Date updatedAt;
    private Date startedAt;
    private Date finishedAt;
    private Date committedAt;
    private String coverage;
    private Integer duration;
    private String webUrl;
    private DetailedStatus detailedStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PipelineStatus getStatus() {
        return status;
    }

    public void setStatus(PipelineStatus status) {
        this.status = status;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getBeforeSha() {
        return beforeSha;
    }

    public void setBeforeSha(String beforeSha) {
        this.beforeSha = beforeSha;
    }

    public Boolean getTag() {
        return tag;
    }

    public void setTag(Boolean tag) {
        this.tag = tag;
    }

    public String getYamlErrors() {
        return yamlErrors;
    }

    public void setYamlErrors(String yamlErrors) {
        this.yamlErrors = yamlErrors;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void setUpdatedAt(Date updated_at) {
        this.updatedAt = updated_at;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date started_at) {
        this.startedAt = started_at;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finished_at) {
        this.finishedAt = finished_at;
    }

    public Date getCommittedAt() {
        return committedAt;
    }

    public void setCommittedAt(Date committed_at) {
        this.committedAt = committed_at;
    }

    /**
     * @deprecated Replaced by {@link #getUpdatedAt()}
     * @return the updated at Date
     */
    @Deprecated
    @JsonIgnore
    public Date getUpdated_at() {
        return updatedAt;
    }

    /**
     * @deprecated Replaced by {@link #setUpdatedAt(Date)}
     * @param updatedAt new updated at value
     */
    @Deprecated
    @JsonIgnore
    public void setUpdated_at(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @deprecated Replaced by {@link #getStartedAt()}
     * @return the started at Date
     */
    @Deprecated
    @JsonIgnore
    public Date getStarted_at() {
        return startedAt;
    }

    /**
     * @deprecated Replaced by {@link #setStartedAt(Date)}
     * @param startedAt new started at value
     */
    @Deprecated
    @JsonIgnore
    public void setStarted_at(Date startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * @deprecated Replaced by {@link #getFinishedAt()}
     * @return the finished at Date
     */
    @Deprecated
    @JsonIgnore
    public Date getFinished_at() {
        return finishedAt;
    }

    /**
     * @deprecated Replaced by {@link #setFinishedAt(Date)}
     * @param finishedAt new finished at value
     */
    @Deprecated
    @JsonIgnore
    public void setFinished_at(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    /**
     * @deprecated Replaced by {@link #getCommittedAt()}
     * @return the committed at Date
     */
    @Deprecated
    @JsonIgnore
    public Date getCommitted_at() {
        return committedAt;
    }

    /**
     * @deprecated Replaced by {@link #setCommittedAt(Date)}
     * @param committedAt new committed at value
     */
    @Deprecated
    @JsonIgnore
    public void setCommitted_at(Date committedAt) {
        this.committedAt = committedAt;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public DetailedStatus getDetailedStatus() {
        return detailedStatus;
    }

    public void setDetailedStatus(DetailedStatus detailedStatus) {
        this.detailedStatus = detailedStatus;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
