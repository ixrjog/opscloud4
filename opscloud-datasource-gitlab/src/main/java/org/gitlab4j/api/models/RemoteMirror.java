package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;

public class RemoteMirror {

    private Long id;
    private Boolean enabled;
    private String lastError;
    private Date lastSuccessfulUpdateAt;
    private Date lastUpdateAt;
    private Date lastUpdateStartedAt;
    private Boolean onlyProtectedBranches;
    private Boolean keepDivergentRefs;
    private String updateStatus;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public Date getLastSuccessfulUpdateAt() {
        return lastSuccessfulUpdateAt;
    }

    public void setLastSuccessfulUpdateAt(Date lastSuccessfulUpdateAt) {
        this.lastSuccessfulUpdateAt = lastSuccessfulUpdateAt;
    }

    public Date getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(Date lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public Date getLastUpdateStartedAt() {
        return lastUpdateStartedAt;
    }

    public void setLastUpdateStartedAt(Date lastUpdateStartedAt) {
        this.lastUpdateStartedAt = lastUpdateStartedAt;
    }

    public Boolean getOnlyProtectedBranches() {
        return onlyProtectedBranches;
    }

    public void setOnlyProtectedBranches(Boolean onlyProtectedBranches) {
        this.onlyProtectedBranches = onlyProtectedBranches;
    }

    public Boolean getKeepDivergentRefs() {
        return keepDivergentRefs;
    }

    public void setKeepDivergentRefs(Boolean keepDivergentRefs) {
        this.keepDivergentRefs = keepDivergentRefs;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return JacksonJson.toJsonString(this);
    }
}
