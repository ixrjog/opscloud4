package com.sdg.cmdb.domain.ci;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

public class CiBuildDO implements Serializable {

    private static final long serialVersionUID = -2639240641048774074L;

    public CiBuildDO() {
    }

    public CiBuildDO(CiJobVO ciJobVO, UserDO userDO, String parameters, boolean deploy) {
        this.jobId = ciJobVO.getId();
        if(deploy){
            this.jobName = ciJobVO.getDeployJobName();
        }else{
            this.jobName = ciJobVO.getJobName();
            this.versionName = ciJobVO.getVersionName();
            this.versionDesc = ciJobVO.getVersionDesc();
        }
        this.displayName = userDO.getUsername() + "<" + userDO.getDisplayName() + ">";
        this.mail = userDO.getMail();
        this.mobile = userDO.getMobile();
        this.parameters = parameters;
    }

    // 构建编号为队列
    public static final int BUILD_NUMBER_QUEUE = -1;

    private long id;

    private long jobId;

    private String jobName;

    private String parameters;

    private String displayName;

    private String mail;

    private String mobile;

    private int buildNumber;

    private String buildPhase;

    private String buildStatus;

    /**
     * 本次build的版本名称
     */
    private String versionName;

    private String versionDesc;

    private String commit;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
