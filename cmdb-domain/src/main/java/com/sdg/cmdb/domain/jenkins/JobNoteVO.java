package com.sdg.cmdb.domain.jenkins;


import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JobNoteVO implements Serializable {


    private static final long serialVersionUID = -4174953958806516603L;
    private String name;
    private String url;
    private BuildVO build;

    // e.g. http://app.ci.51xianqu.net/job/one.distribution.daily/56/
    private String buildUrl;
    // e.g. http://app.ci.51xianqu.net/job/one.distribution.daily/default/65/console
    private String buildConsoleUrl;
    private String buildPhase;
    private String buildStatus;

    private boolean notice;

    private String commit;
    private String branch;

    private int webHook;

    private String timeView;
    private String gmtCreate;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public JobNoteVO() {

    }

    public JobNoteVO(JobNoteDO jobNoteDO, String jenkinsHost, int envType) {
        this.name = jobNoteDO.getJobName();
        this.buildUrl = jobNoteDO.getBuildFullUrl();
        if (envType == GitlabWebHooksDO.HooksTypeEnum.ft.getCode())
            this.buildConsoleUrl = jenkinsHost + "/" + jobNoteDO.getJobUrl() + "default/" + jobNoteDO.getBuildNumber() + "/console";
        if (envType == GitlabWebHooksDO.HooksTypeEnum.android.getCode())
            this.buildConsoleUrl = jenkinsHost + "/" + jobNoteDO.getJobUrl() + jobNoteDO.getBuildNumber() + "/console";
        if (envType == GitlabWebHooksDO.HooksTypeEnum.ios.getCode())
            this.buildConsoleUrl = jenkinsHost + "/" + jobNoteDO.getJobUrl() + jobNoteDO.getBuildNumber() + "/console";
        this.buildPhase = jobNoteDO.getBuildPhase();
        this.buildStatus = jobNoteDO.getBuildStatus();
        this.gmtCreate = jobNoteDO.getGmtCreate();
        this.branch = jobNoteDO.getScmBranch();
        this.commit = jobNoteDO.getScmCommit();
        this.notice = jobNoteDO.isNotice();
    }

    @Override
    public String toString() {
        return "JobNotificationsVO{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", build='" + build.toString() + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BuildVO getBuild() {
        return build;
    }

    public void setBuild(BuildVO build) {
        this.build = build;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getBuildUrl() {
        return buildUrl;
    }

    public void setBuildUrl(String buildUrl) {
        this.buildUrl = buildUrl;
    }

    public String getBuildConsoleUrl() {
        return buildConsoleUrl;
    }

    public void setBuildConsoleUrl(String buildConsoleUrl) {
        this.buildConsoleUrl = buildConsoleUrl;
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

    public String getTimeView() {
        return timeView;
    }

    public void setTimeView(String timeView) {
        this.timeView = timeView;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
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
}
