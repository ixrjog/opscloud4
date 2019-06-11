package com.sdg.cmdb.domain.gitlab;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.gitlab.v1.GitlabWebHooks;
import lombok.Data;

import java.io.Serializable;

@Data
public class GitlabWebHooksDO implements Serializable {
    private static final long serialVersionUID = -6986307556704662509L;

    private long id;
    private int projectId;
    private String projectName;
    private String projectDescription;
    private String commitBefore;
    private String commitAfter;
    private String ref;
    private int userId;
    private String userName;
    private String email;

    private String sshUrl;
    private String httpUrl;
    private String homepage;
    private int totalCommitsCount;
    private boolean triggerBuild = false; // 是否触发构建
    private String jobName;
    private String webhooksBody;
    private String gmtCreate;
    private String gmtModify;

    public GitlabWebHooksDO(GitlabWebHooks gitlabWebHooks) {
        this.projectId = gitlabWebHooks.getProject_id();
        this.projectName = gitlabWebHooks.getProject().getName();
        this.projectDescription = gitlabWebHooks.getProject().getDescription();
        this.commitBefore = gitlabWebHooks.getBefore();
        this.commitAfter = gitlabWebHooks.getAfter();
        this.ref = gitlabWebHooks.getRef();
        this.userId = gitlabWebHooks.getUser_id();
        this.userName = gitlabWebHooks.getUser_name();
        this.email = gitlabWebHooks.getUser_email();
        this.sshUrl = gitlabWebHooks.getProject().getSsh_url();
        this.httpUrl = gitlabWebHooks.getProject().getHttp_url();
        this.homepage = gitlabWebHooks.getProject().getHomepage();
        this.totalCommitsCount = gitlabWebHooks.getTotal_commits_count();
        this.webhooksBody = JSON.toJSONString(gitlabWebHooks);
    }

    public GitlabWebHooksDO() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
