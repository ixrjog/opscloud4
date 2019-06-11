package com.sdg.cmdb.domain.gitlab;


import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.gitlab.api.models.GitlabProject;

import java.io.Serializable;

@Data
public class GitlabProjectDO implements Serializable {

    private int id;
    private int projectId;
    private String name;
    private String nameWithNamespace;
    private String description;
    private String visibility;
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
        this.visibility = gitlabProject.getVisibility();
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

    public GitlabProjectDO(int id, GitlabProject gitlabProject) {
        this.id = id;
        this.projectId = gitlabProject.getId();
        this.name = gitlabProject.getName();
        this.nameWithNamespace = gitlabProject.getNameWithNamespace();
        this.description = gitlabProject.getDescription();
        this.visibility = gitlabProject.getVisibility();
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
