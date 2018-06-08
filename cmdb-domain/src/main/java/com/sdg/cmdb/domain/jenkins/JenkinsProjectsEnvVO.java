package com.sdg.cmdb.domain.jenkins;

import java.io.Serializable;
import java.util.List;

public class JenkinsProjectsEnvVO implements Serializable {
    private static final long serialVersionUID = 4871483999091147069L;

    private long id;

    private long projectId;

    private int envType;

    private long jobsId;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    private List<JenkinsEnvParamDO> params;

    public JenkinsProjectsEnvVO(JenkinsProjectsEnvDO jenkinsProjectsEnvDO, List<JenkinsEnvParamDO> params) {
        this.id = jenkinsProjectsEnvDO.getId();
        this.projectId = jenkinsProjectsEnvDO.getProjectId();
        this.envType = jenkinsProjectsEnvDO.getEnvType();
        this.jobsId = jenkinsProjectsEnvDO.getJobsId();
        this.content = jenkinsProjectsEnvDO.getContent();
        this.gmtCreate = jenkinsProjectsEnvDO.getGmtCreate();
        this.gmtModify = jenkinsProjectsEnvDO.getGmtModify();
        this.params = params;
    }

    public JenkinsProjectsEnvVO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }

    public long getJobsId() {
        return jobsId;
    }

    public void setJobsId(long jobsId) {
        this.jobsId = jobsId;
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

    public List<JenkinsEnvParamDO> getParams() {
        return params;
    }

    public void setParams(List<JenkinsEnvParamDO> params) {
        this.params = params;
    }
}
