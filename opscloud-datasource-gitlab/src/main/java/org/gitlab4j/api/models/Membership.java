package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class Membership {

    private Long sourceId;
    private String sourceName;
    private MembershipSourceType sourceType;
    private AccessLevel accessLevel;

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public MembershipSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(MembershipSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
