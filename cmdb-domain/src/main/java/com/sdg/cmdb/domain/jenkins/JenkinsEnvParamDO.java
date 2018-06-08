package com.sdg.cmdb.domain.jenkins;

import java.io.Serializable;

public class JenkinsEnvParamDO implements Serializable {
    private static final long serialVersionUID = -3840915957426159847L;


    private long id;

    private long envId;

    private String paramName;

    private String paramValue;

    private int paramType;

    private String content;

    private long jobsId;

    private String gmtCreate;

    private String gmtModify;

    public JenkinsEnvParamDO(BaseParamDO baseParamDO,long envId){
        this.paramName = baseParamDO.getParamName();
        this.paramValue = baseParamDO.getParamValue();
        this.paramType = baseParamDO.getParamType();
        this.content = baseParamDO.getContent();
        this.envId = envId;
    }

    public JenkinsEnvParamDO(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEnvId() {
        return envId;
    }

    public void setEnvId(long envId) {
        this.envId = envId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public int getParamType() {
        return paramType;
    }

    public void setParamType(int paramType) {
        this.paramType = paramType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public long getJobsId() {
        return jobsId;
    }

    public void setJobsId(long jobsId) {
        this.jobsId = jobsId;
    }
}
