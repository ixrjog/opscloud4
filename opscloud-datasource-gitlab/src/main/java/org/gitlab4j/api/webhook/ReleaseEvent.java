package org.gitlab4j.api.webhook;

import org.gitlab4j.api.utils.JacksonJson;

public class ReleaseEvent extends AbstractEvent {

    public static final String X_GITLAB_EVENT = "Release Hook";
    public static final String OBJECT_KIND = "release";

    private Long id;
    private String createdAt;
    private String description;
    private String name;
    private String releasedAt;
    private String tag;
    private EventProject project;
    private String url;
    private String action;
    private EventReleaseAssets assets;
    private EventCommit commit;

    @Override
	public String getObjectKind() {
        return (OBJECT_KIND);
    }

    public void setObjectKind(String objectKind) {
        if (!OBJECT_KIND.equals(objectKind))
            throw new RuntimeException("Invalid object_kind (" + objectKind + "), must be '" + OBJECT_KIND + "'");
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(final String releasedAt) {
        this.releasedAt = releasedAt;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(final String tag) {
        this.tag = tag;
    }

    public EventProject getProject() {
        return project;
    }

    public void setProject(final EventProject project) {
        this.project = project;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public EventReleaseAssets getAssets() {
        return assets;
    }

    public void setAssets(final EventReleaseAssets assets) {
        this.assets = assets;
    }

    public EventCommit getCommit() {
        return commit;
    }

    public void setCommit(final EventCommit commit) {
        this.commit = commit;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
