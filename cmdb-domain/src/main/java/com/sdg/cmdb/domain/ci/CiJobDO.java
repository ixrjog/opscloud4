package com.sdg.cmdb.domain.ci;

import java.io.Serializable;

public class CiJobDO implements Serializable {
    private static final long serialVersionUID = 5899629540817517884L;

    private long id;

    private String name;

    private String content;

    private long appId;

    private int ciType;

    /**
     * 发布分支，留空可选
     */
    private String branch;

    /**
     * 组机分组，可选参数
     */
    private String hostPattern;

    private int envType;

    private String jobName;

    private String jobTemplate;

    private int jobVersion;

    private String deployJobName;

    private String deployJobTemplate;

    private int deployJobVersion;

    private int rollbackType;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public int getCiType() {
        return ciType;
    }

    public void setCiType(int ciType) {
        this.ciType = ciType;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getHostPattern() {
        return hostPattern;
    }

    public void setHostPattern(String hostPattern) {
        this.hostPattern = hostPattern;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobTemplate() {
        return jobTemplate;
    }

    public void setJobTemplate(String jobTemplate) {
        this.jobTemplate = jobTemplate;
    }

    public int getJobVersion() {
        return jobVersion;
    }

    public void setJobVersion(int jobVersion) {
        this.jobVersion = jobVersion;
    }

    public int getRollbackType() {
        return rollbackType;
    }

    public void setRollbackType(int rollbackType) {
        this.rollbackType = rollbackType;
    }

    public String getDeployJobName() {
        return deployJobName;
    }

    public void setDeployJobName(String deployJobName) {
        this.deployJobName = deployJobName;
    }

    public String getDeployJobTemplate() {
        return deployJobTemplate;
    }

    public void setDeployJobTemplate(String deployJobTemplate) {
        this.deployJobTemplate = deployJobTemplate;
    }

    public int getDeployJobVersion() {
        return deployJobVersion;
    }

    public void setDeployJobVersion(int deployJobVersion) {
        this.deployJobVersion = deployJobVersion;
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
