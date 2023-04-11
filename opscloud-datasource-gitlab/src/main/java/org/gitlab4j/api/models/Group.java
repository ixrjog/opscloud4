
package org.gitlab4j.api.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;
import java.util.List;

public class Group extends AbstractGroup<Group> {

    public class Statistics {
        private Long storageSize;
        private Long repositorySize;
        private Long lfsObjectsSize;
        private Long jobArtifactsSize;

        public Long getStorageSize() {
            return storageSize;
        }

        public void setStorageSize(Long storageSize) {
            this.storageSize = storageSize;
        }

        public Long getRepositorySize() {
            return repositorySize;
        }

        public void setRepositorySize(Long repositorySize) {
            this.repositorySize = repositorySize;
        }

        public Long getLfsObjectsSize() {
            return lfsObjectsSize;
        }

        public void setLfsObjectsSize(Long lfsObjectsSize) {
            this.lfsObjectsSize = lfsObjectsSize;
        }

        public Long getJobArtifactsSize() {
            return jobArtifactsSize;
        }

        public void setJobArtifactsSize(Long jobArtifactsSize) {
            this.jobArtifactsSize = jobArtifactsSize;
        }
    }


    private String path;
    private String description;
    private Visibility visibility;
    private Boolean lfsEnabled;
    private Boolean requestAccessEnabled;
    private Long parentId;
    private Integer sharedRunnersMinutesLimit;
    private Statistics statistics;
    private List<Project> projects;
    private List<Project> sharedProjects;
    private Date createdAt;
    private String runnersToken;

    @JsonSerialize(using = JacksonJson.DateOnlySerializer.class)
    private Date markedForDeletionOn;

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Boolean getLfsEnabled() {
        return lfsEnabled;
    }

    public void setLfsEnabled(Boolean lfsEnabled) {
        this.lfsEnabled = lfsEnabled;
    }

    public Boolean getRequestAccessEnabled() {
        return requestAccessEnabled;
    }

    public void setRequestAccessEnabled(Boolean requestAccessEnabled) {
        this.requestAccessEnabled = requestAccessEnabled;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSharedRunnersMinutesLimit() {
        return sharedRunnersMinutesLimit;
    }

    public void setSharedRunnersMinutesLimit(Integer sharedRunnersMinutesLimit) {
        this.sharedRunnersMinutesLimit = sharedRunnersMinutesLimit;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public List<Project> getProjects() {
        return (projects);
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Project> getSharedProjects() {
        return (sharedProjects);
    }

    public void setSharedProjects(List<Project> sharedProjects) {
        this.sharedProjects = sharedProjects;
    }

    public Date getMarkedForDeletionOn() {
        return markedForDeletionOn;
    }

    public void setMarkedForDeletionOn(Date markedForDeletionOn) {
        this.markedForDeletionOn = markedForDeletionOn;
    }

    public Date getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
	this.createdAt = createdAt;
    }

    public String getRunnersToken() {
        return runnersToken;
    }

    public void setRunnersToken(String runnersToken) {
        this.runnersToken = runnersToken;
    }

    public Group withPath(String path) {
        this.path = path;
        return this;
    }

    public Group withDescription(String description) {
        this.description = description;
        return this;
    }

    public Group withVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Group withlfsEnabled(boolean lfsEnabled) {
        this.lfsEnabled = lfsEnabled;
        return this;
    }

    public Group withRequestAccessEnabled(boolean requestAccessEnabled) {
        this.requestAccessEnabled = requestAccessEnabled;
        return this;
    }

    public Group withParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public Group withSharedRunnersMinutesLimit(Integer minutesLimit) {
        this.sharedRunnersMinutesLimit = minutesLimit;
        return this;
    }

    public Group withStatistics(Statistics statistics) {
        this.statistics = statistics;
        return this;
    }

    public Group withProjects(List<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Group withSharedProjects(List<Project> sharedProjects) {
        this.sharedProjects = sharedProjects;
        return this;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
