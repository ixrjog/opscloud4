package com.sdg.cmdb.domain.jenkins;

import java.io.Serializable;

public class JobNoteDO implements Serializable {
    private static final long serialVersionUID = 2497741031435767825L;

    private long id;
    private String jobName;

    private String jobUrl;

    private String buildFullUrl;

    private int buildNumber;

    private String buildPhase;

    private String buildStatus;

    private String buildUrl;

    private String scmUrl;

    private String scmBranch;

    private String scmCommit;

    /**
     * dingtalk通知
     */
    private boolean notice;

    private int webHook = 0;

    private String gmtCreate;

    private String gmtModify;

    public JobNoteDO(JobNoteVO jobNote) {
        this.jobName = jobNote.getName();
        this.jobUrl = jobNote.getUrl();
        BuildVO build = jobNote.getBuild();
        this.buildFullUrl = build.getFullUrl();
        this.buildNumber = build.getNumber();
        this.buildPhase = build.getPhase();
        this.buildStatus = build.getStatus();
        this.buildUrl = build.getUrl();
        this.scmUrl = build.getScmUrl();
        this.scmBranch = build.getScmBranch();
        this.scmCommit = build.getScmCommit();
        this.webHook = jobNote.getWebHook();
    }

    public JobNoteDO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isNotice() {
        return notice;
    }

    public void setNotice(boolean notice) {
        this.notice = notice;
    }

    public int getWebHook() {
        return webHook;
    }

    public void setWebHook(int webHook) {
        this.webHook = webHook;
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
