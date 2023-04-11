package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.List;

public class ExternalStatusCheck {

    private Long id;
    private String name;
    private Long projectId;
    private String externalUrl;
    private List<ExternalStatusCheckProtectedBranch> protectedBranches;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public List<ExternalStatusCheckProtectedBranch> getProtectedBranches() {
        return protectedBranches;
    }

    public void setProtectedBranches(List<ExternalStatusCheckProtectedBranch> protectedBranches) {
        this.protectedBranches = protectedBranches;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
