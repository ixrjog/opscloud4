package com.sdg.cmdb.domain.jenkins;

import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;

import java.io.Serializable;
import java.util.List;

public class JenkinsJobBuildVO implements Serializable {
    private static final long serialVersionUID = -6163034869049329272L;

    private long id;

    private String username;

    private String email;

    private String mobile;

    private int buildType;

    private long jobId;

    private String jobName;

    private long webHookId;

    private int buildNumber;

    private String qrcode;

    private String cdnUrl;

    List<JobNoteVO> jobNotes;

    List<JobBuildParamDO> paramList;

    List<BuildArtifactDO> artifacts;

    private GitlabWebHooksDO webHooks;

    private String gmtCreate;

    private String gmtModify;
//List<BuildArtifactDO> artifacts
    public JenkinsJobBuildVO(JenkinsJobBuildDO jobBuildDO, List<JobNoteVO> jobNotes, GitlabWebHooksDO webHooks, List<JobBuildParamDO> paramList,List<BuildArtifactDO> artifacts) {
        this.id = jobBuildDO.getId();
        this.jobName = jobBuildDO.getJobName();
        this.username = jobBuildDO.getUsername();
        this.email = jobBuildDO.getEmail();
        this.mobile = jobBuildDO.getMobile();
        this.buildType = jobBuildDO.getBuildType();
        this.jobNotes = jobNotes;
        this.buildNumber = jobBuildDO.getBuildNumber();
        this.webHooks = webHooks;
        this.gmtCreate = jobBuildDO.getGmtCreate();
        this.paramList = paramList;
        this.artifacts = artifacts;
    }

    public JenkinsJobBuildVO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getBuildType() {
        return buildType;
    }

    public void setBuildType(int buildType) {
        this.buildType = buildType;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public long getWebHookId() {
        return webHookId;
    }

    public void setWebHookId(long webHookId) {
        this.webHookId = webHookId;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
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

    public List<JobNoteVO> getJobNotes() {
        return jobNotes;
    }

    public void setJobNotes(List<JobNoteVO> jobNotes) {
        this.jobNotes = jobNotes;
    }

    public GitlabWebHooksDO getWebHooks() {
        return webHooks;
    }

    public void setWebHooks(GitlabWebHooksDO webHooks) {
        this.webHooks = webHooks;
    }

    public List<JobBuildParamDO> getParamList() {
        return paramList;
    }

    public void setParamList(List<JobBuildParamDO> paramList) {
        this.paramList = paramList;
    }

    public List<BuildArtifactDO> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<BuildArtifactDO> artifacts) {
        this.artifacts = artifacts;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }
}
