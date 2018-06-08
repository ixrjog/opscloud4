package com.sdg.cmdb.domain.jenkins;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

public class JenkinsJobBuildDO implements Serializable {
    private static final long serialVersionUID = 4631311073719691569L;

    // 构建编号为队列
    public static final int BUILD_NUMBER_QUEUE = -1;

    private long id;

    private String username;

    private String email;

    private String mobile;

    private int buildType;

    private long jobId;

    private String jobName;

    private long webHookId;

    private int buildNumber;


    private String gmtCreate;

    private String gmtModify;

    public JenkinsJobBuildDO(JenkinsJobDO jenkinsJob, UserDO userDO, long webHookId, int buildNumber) {
        if(userDO != null) {
            this.username = userDO.getUsername();
            this.email = userDO.getMail();
            this.mobile = userDO.getMobile();
        }

        this.buildType = jenkinsJob.getBuildType();
        this.jobId = jenkinsJob.getId();
        this.jobName = jenkinsJob.getJobName();
        this.webHookId = webHookId;
        this.buildNumber = buildNumber;
    }


    public JenkinsJobBuildDO() {

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
}
