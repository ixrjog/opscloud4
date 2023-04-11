package org.gitlab4j.api.systemhooks;

import org.gitlab4j.api.models.Visibility;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class ProjectSystemHookEvent extends AbstractSystemHookEvent {

    public static final String PROJECT_CREATE_EVENT = "project_create";
    public static final String PROJECT_DESTROY_EVENT = "project_destroy";
    public static final String PROJECT_RENAME_EVENT = "project_rename";
    public static final String PROJECT_TRANSFER_EVENT = "project_transfer";
    public static final String PROJECT_UPDATE_EVENT = "project_update";

    private Date createdAt;
    private Date updatedAt;
    private String eventName;
    private String name;
    private String ownerEmail;
    private String ownerName;
    private String path;
    private Long projectId;
    private String pathWithNamespace;
    private Visibility projectVisibility;
    private String oldPathWithNamespace;

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

    @Override
	public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerEmail() {
        return this.ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPathWithNamespace() {
        return pathWithNamespace;
    }

    public void setPathWithNamespace(String pathWithNamespace) {
        this.pathWithNamespace = pathWithNamespace;
    }

    public Visibility getProjectVisibility() {
        return projectVisibility;
    }

    public void setProjectVisibility(Visibility projectVisibility) {
        this.projectVisibility = projectVisibility;
    }

    public String getOldPathWithNamespace() {
        return oldPathWithNamespace;
    }

    public void setOldPathWithNamespace(String oldPathWithNamespace) {
        this.oldPathWithNamespace = oldPathWithNamespace;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
