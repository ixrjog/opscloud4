package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class BranchAccessLevel {

    private Long id;
    private AccessLevel accessLevel;
    private String accessLevelDescription;
    private Long userId;
    private Long groupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccessLevel getAccessLevel() {
        return this.accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getAccessLevelDescription() {
        return this.accessLevelDescription;
    }

    public void setAccessLevelDescription(String accessLevelDescription) {
        this.accessLevelDescription = accessLevelDescription;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
