package org.gitlab4j.api.webhook;

import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class EventSnippet {

    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private Long projectId;
    private Date createdAt;
    private Date updatedAt;
    private String fileName;
    private Date expiresAt;
    private String type;
    private AccessLevel visibilityLevel;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AccessLevel getVisibilityLevel() {
        return visibilityLevel;
    }

    public void setVisibilityLevel(AccessLevel visibilityLevel) {
        this.visibilityLevel = visibilityLevel;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
