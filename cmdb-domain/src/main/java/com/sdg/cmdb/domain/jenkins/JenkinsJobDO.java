package com.sdg.cmdb.domain.jenkins;

import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;
import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

public class JenkinsJobDO implements Serializable {
    private static final long serialVersionUID = -9179771279259269002L;

    private long id;

    private String jobName;

    private String content;

    private int jobEnvType;

    private String repositoryUrl;

    private int buildType;

    // 是否创建job
    private boolean created = false;

    private String gmtCreate;

    private String gmtModify;

    //GitlabWebHooksDO webHooks
    public JenkinsJobDO(GitlabWebHooksDO webHooks, String fullJobName, int envType, int buildType) {
        this.jobName = fullJobName;
        this.content = webHooks.getRepositoryDescription();
        //this.jobEnvType = 0;
        this.repositoryUrl = webHooks.getRepositoryUrl();
        this.buildType = buildType;
        this.jobEnvType = envType;

    }

    public JenkinsJobDO(JenkinsProjectsDO jenkinsProjectsDO, JenkinsProjectsEnvDO envDO, String jobName) {
        this.jobName = jobName;
        this.content = jenkinsProjectsDO.getContent();
        this.repositoryUrl = jenkinsProjectsDO.getRepositoryUrl();
        this.jobEnvType = envDO.getEnvType();
        this.buildType = jenkinsProjectsDO.getBuildType();
    }

    public JenkinsJobDO(JenkinsJobVO jenkinsJobVO) {
        this.id = jenkinsJobVO.getId();
        this.jobName = jenkinsJobVO.getJobName();
        this.content = jenkinsJobVO.getContent();
        this.jobEnvType = jenkinsJobVO.getJobEnvType();
        this.repositoryUrl = jenkinsJobVO.getRepositoryUrl();
        this.buildType = jenkinsJobVO.getBuildType();
        this.gmtCreate = jenkinsJobVO.getGmtCreate();
        this.gmtModify = jenkinsJobVO.getGmtModify();
    }

    public JenkinsJobDO() {

    }

    public enum JobEnvTypeEnum {
        //0 保留／在组中代表的是所有权限
        prod(1, "prod"),
        daily(2, "daily"),
        gray(3, "gray"),
        onlineDev(4, "onlineDev"),
        release(5, "release"),
        all(6, "all"),
        debug(7, "debug");
        private int code;
        private String desc;

        JobEnvTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getJobEnvTypeName(int code) {
            for (JobEnvTypeEnum jobEnvTypeEnum : JobEnvTypeEnum.values()) {
                if (jobEnvTypeEnum.getCode() == code) {
                    return jobEnvTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    @Override
    public String toString() {
        return "JenkinsJobDO{" +
                ", id=" + id +
                ", jobName='" + jobName + '\'' +
                ", content='" + content + '\'' +
                ", jobEnvType=" + jobEnvType +
                ", repositoryUrl='" + repositoryUrl + '\'' +
                ", buildType=" + buildType +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
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

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
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
