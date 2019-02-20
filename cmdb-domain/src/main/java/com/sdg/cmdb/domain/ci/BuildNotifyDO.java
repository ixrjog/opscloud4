package com.sdg.cmdb.domain.ci;

import com.sdg.cmdb.domain.ci.jenkins.Build;
import com.sdg.cmdb.domain.ci.jenkins.Notify;
import com.sdg.cmdb.domain.ci.jenkins.Scm;


import java.io.Serializable;

public class BuildNotifyDO implements Serializable {
    private static final long serialVersionUID = 2497741031435767825L;

    private long id;

    private long buildId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务短路径
     */
    private String jobUrl;

    /**
     * 完整build路径  "full_url": "http://localhost:8080/job/asgard/18/"
     */
    private String buildFullUrl;

    private int buildNumber;

    private String buildPhase;

    private String buildStatus;

    /**
     * build短路径  "url": "job/asgard/18/"
     */
    private String buildUrl;

    private String scmUrl;

    private String scmBranch;

    private String scmCommit;

    /**
     * dingtalk通知
     */
    private boolean dingtalk;

    private String gmtCreate;

    private String gmtModify;

    public BuildNotifyDO(long buildId,Notify notify) {
        this.buildId = buildId;
        this.jobName = notify.getName();
        this.jobUrl = notify.getUrl();
        Build build = notify.getBuild();
        if (build != null) {
            this.buildFullUrl = build.getFullUrl();
            this.buildNumber = build.getNumber();
            this.buildPhase = build.getPhase();
            this.buildStatus = build.getStatus();
            this.buildUrl = build.getUrl();
            Scm scm = build.getScm();
            if (scm != null) {
                this.scmUrl = scm.getUrl();
                this.scmBranch = scm.getBranch();
                this.scmCommit = scm.getCommit();
            }
        }

    }

    public BuildNotifyDO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBuildId() {
        return buildId;
    }

    public void setBuildId(long buildId) {
        this.buildId = buildId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public String getBuildFullUrl() {
        return buildFullUrl;
    }

    public void setBuildFullUrl(String buildFullUrl) {
        this.buildFullUrl = buildFullUrl;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getBuildPhase() {
        return buildPhase;
    }

    public void setBuildPhase(String buildPhase) {
        this.buildPhase = buildPhase;
    }

    public String getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }

    public String getBuildUrl() {
        return buildUrl;
    }

    public void setBuildUrl(String buildUrl) {
        this.buildUrl = buildUrl;
    }

    public String getScmUrl() {
        return scmUrl;
    }

    public void setScmUrl(String scmUrl) {
        this.scmUrl = scmUrl;
    }

    public String getScmBranch() {
        return scmBranch;
    }

    public void setScmBranch(String scmBranch) {
        this.scmBranch = scmBranch;
    }

    public String getScmCommit() {
        return scmCommit;
    }

    public void setScmCommit(String scmCommit) {
        this.scmCommit = scmCommit;
    }

    public boolean isDingtalk() {
        return dingtalk;
    }

    public void setDingtalk(boolean dingtalk) {
        this.dingtalk = dingtalk;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }
}
