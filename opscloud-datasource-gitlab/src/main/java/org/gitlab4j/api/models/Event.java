
package org.gitlab4j.api.models;

import org.gitlab4j.api.Constants.TargetType;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class Event {

    private String actionName;
    private Author author;
    private Long authorId;
    private String authorUsername;
    private EventData data;
    private Long projectId;
    private Long targetId;
    private Long targetIid;
    private String targetTitle;
    private TargetType targetType;
    private String title;
    private Date createdAt;

    private Note note;
    private PushData pushData;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public EventData getData() {
        return data;
    }

    public void setData(EventData data) {
        this.data = data;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getTargetIid() {
        return targetIid;
    }

    public void setTargetIid(Long targetIid) {
        this.targetIid = targetIid;
    }

    public String getTargetTitle() {
        return targetTitle;
    }

    public void setTargetTitle(String targetTitle) {
        this.targetTitle = targetTitle;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
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

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public PushData getPushData() {
        return pushData;
    }

    public void setPushData(PushData pushData) {
        this.pushData = pushData;
    }

    public Event withActionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    public Event withAuthor(Author author) {
        this.author = author;
        return this;
    }

    public Event withAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }

    public Event withAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
        return this;
    }

    public Event withData(EventData data) {
        this.data = data;
        return this;
    }

    public Event withProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public Event withTargetId(Long targetId) {
        this.targetId = targetId;
        return this;
    }

    public Event withTargetIid(Long targetIid) {
        this.targetIid = targetIid;
        return this;
    }

    public Event withTargetTitle(String targetTitle) {
        this.targetTitle = targetTitle;
        return this;
    }

    public Event withTargetType(TargetType targetType) {
        this.targetType = targetType;
        return this;
    }

    public Event withTitle(String title) {
        this.title = title;
        return this;
    }

    public Event withCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
