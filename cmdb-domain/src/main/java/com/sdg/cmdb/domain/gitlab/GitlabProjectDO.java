package com.sdg.cmdb.domain.gitlab;


import com.alibaba.fastjson.JSON;
import org.gitlab.api.models.GitlabProject;

import java.io.Serializable;

public class GitlabProjectDO implements Serializable {

    private int id;
    private int projectId;
    private String name;
    private String nameWithNamespace;
    private String description;
    private String defaultBranch;
    private int ownerId;
    private String ownerUsername;
    private String sshUrl;
    private String webUrl;
    private String httpUrl;
    private String createedAt;
    private String lastActivityAt;

    public GitlabProjectDO(GitlabProject gitlabProject) {
        this.projectId = gitlabProject.getId();
        this.name = gitlabProject.getName();
        this.nameWithNamespace = gitlabProject.getNameWithNamespace();
        this.description = gitlabProject.getDescription();
        this.defaultBranch = gitlabProject.getDefaultBranch();
        if (gitlabProject.getOwner() != null) {
            this.ownerId = gitlabProject.getOwner().getId();
            this.ownerUsername = gitlabProject.getOwner().getUsername();
        }
        this.sshUrl = gitlabProject.getSshUrl();
        this.webUrl = gitlabProject.getWebUrl();
        this.httpUrl = gitlabProject.getHttpUrl();
        this.createedAt = gitlabProject.getCreatedAt().toString();
        this.lastActivityAt = gitlabProject.getLastActivityAt().toString();
    }

    public GitlabProjectDO() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameWithNamespace() {
        return nameWithNamespace;
    }

    public void setNameWithNamespace(String nameWithNamespace) {
        this.nameWithNamespace = nameWithNamespace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getCreateedAt() {
        return createedAt;
    }

    public void setCreateedAt(String createedAt) {
        this.createedAt = createedAt;
    }

    public String getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(String lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }



    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
