package com.sdg.cmdb.domain.jenkins;

import com.sdg.cmdb.domain.auth.UserDO;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;

public class JenkinsJobVO implements Serializable {
    private static final long serialVersionUID = -3555423145941722683L;

    private long id;

    private String jobName;

    private String content;

    private int jobEnvType;

    private String repositoryUrl;

    private int buildType;

    private List<UserDO> emailUsers;

    private String gmtCreate;

    private String gmtModify;

    private List<JobParamDO> params;

    @Override
    public String toString() {
        String emailUsers = "";
        if (this.emailUsers != null && this.emailUsers.size() != 0) {
            for (UserDO userDO : this.emailUsers) {
                if (StringUtils.isEmpty(emailUsers)) {
                    emailUsers = userDO.getMail();
                } else {
                    emailUsers += "," + userDO.getMail();
                }
            }
        }

        String params = "";
        for (JobParamDO jobParamDO : this.params) {
            if (StringUtils.isEmpty(params)) {
                params = "\'"+ jobParamDO.getParamName() + ":" + jobParamDO.getParamValue()+"\'";
            } else {
                params += ",\'" + jobParamDO.getParamName() + ":" + jobParamDO.getParamValue()+"\'";
            }
        }

        return "JenkinsJobDO{" +
                ", id=" + id +
                ", jobName='" + jobName + '\'' +
                ", content='" + content + '\'' +
                ", jobEnvType=" + jobEnvType +
                ", repositoryUrl='" + repositoryUrl + '\'' +
                ", buildType=" + buildType +
                ", params=" + params +
                ", emailUsers=" + emailUsers +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    public JenkinsJobVO() {

    }

    public JenkinsJobVO(JenkinsJobDO jenkinsJobDO, List<JobParamDO> params) {
        this.id = jenkinsJobDO.getId();
        this.jobName = jenkinsJobDO.getJobName();
        this.content = jenkinsJobDO.getContent();
        this.jobEnvType = jenkinsJobDO.getJobEnvType();
        this.repositoryUrl = jenkinsJobDO.getRepositoryUrl();
        this.buildType = jenkinsJobDO.getBuildType();
        this.gmtCreate = jenkinsJobDO.getGmtCreate();
        this.gmtModify = jenkinsJobDO.getGmtModify();
        this.params = params;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getJobEnvType() {
        return jobEnvType;
    }

    public void setJobEnvType(int jobEnvType) {
        this.jobEnvType = jobEnvType;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public int getBuildType() {
        return buildType;
    }

    public void setBuildType(int buildType) {
        this.buildType = buildType;
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

    public List<JobParamDO> getParams() {
        return params;
    }

    public void setParams(List<JobParamDO> params) {
        this.params = params;
    }

    public List<UserDO> getEmailUsers() {
        return emailUsers;
    }

    public void setEmailUsers(List<UserDO> emailUsers) {
        this.emailUsers = emailUsers;
    }
}
