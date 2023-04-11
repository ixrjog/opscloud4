
package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class ExternalStatusCheckProtectedBranch {

    private Long id;
    private Long projectId;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private Boolean codeOwnerApprovalRequired;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getCodeOwnerApprovalRequired() {
        return codeOwnerApprovalRequired;
    }

    public void setCodeOwnerApprovalRequired(Boolean codeOwnerApprovalRequired) {
        this.codeOwnerApprovalRequired = codeOwnerApprovalRequired;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
