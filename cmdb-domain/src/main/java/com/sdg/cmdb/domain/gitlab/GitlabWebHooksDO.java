package com.sdg.cmdb.domain.gitlab;

import java.io.Serializable;

public class GitlabWebHooksDO implements Serializable {
    private static final long serialVersionUID = -6986307556704662509L;

    private long id;

    private String commitBefore;

    // 当前提交的sha值
    private String commitAfter;

    private String ref;

    // gitlab userId
    private int userId;

    // 显示名称
    private String userName;

    private String email;


    // gitlab projectId
    private int projectId;

    private String projectName;

    private String projectDescription;

    private String repositoryName;

    private String repositoryUrl;

    private String repositoryDescription;

    private String repositoryHomepage;

    private int totalCommitsCount;

    private int webHooksType;

    /** 是否触发构建
     * 0 不符合构建条件
     * 1 触发
     */
    private int triggerBuild = 0;

    private String gmtCreate;

    private String gmtModify;

    /**
     * 是否触发构建  triggerBuild
     */
    public enum TriggerTypeEnum {
        skip(0, "skip"),
        trigger(1, "trigger");

        private int code;
        private String desc;

        TriggerTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getTriggerTypeName(int code) {
            for (TriggerTypeEnum triggerTypeEnum : TriggerTypeEnum.values()) {
                if (triggerTypeEnum.getCode() == code) {
                    return triggerTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    /**
     * 此类型与buildType一致
     */
    public enum HooksTypeEnum {
        //前端
        ft(0, "ft"),
        android(1, "android"),
        ios(2, "ios"),
        test(3, "test");
        private int code;
        private String desc;

        HooksTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getHooksTypeName(int code) {
            for (HooksTypeEnum hooksTypeEnum : HooksTypeEnum.values()) {
                if (hooksTypeEnum.getCode() == code) {
                    return hooksTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public GitlabWebHooksDO() {

    }

    public GitlabWebHooksDO(GitlabWebHooksVO webHooksVO, String projectName, int hooksType,String email) {
        this.commitBefore = webHooksVO.getBefore();
        this.commitAfter = webHooksVO.getAfter();
        this.ref = webHooksVO.getRef();
        this.userId = webHooksVO.getUserId();
        this.userName = webHooksVO.getUser_name();
        this.email = email;
        this.projectId = webHooksVO.getProjectId();
        this.projectName = projectName.toLowerCase();
        this.projectDescription = webHooksVO.getProjectDescription();
        this.repositoryName = webHooksVO.getRepositoryName().toLowerCase();
        this.repositoryUrl = webHooksVO.getRepositoryUrl();
        this.repositoryDescription = webHooksVO.getRepositoryDescription();
        this.repositoryHomepage = webHooksVO.getRepositoryHomepage();
        this.totalCommitsCount = webHooksVO.getTotalCommitsCount();
        this.webHooksType = hooksType;
    }

    @Override
    public String toString() {
        return "GitlabWebHooksDO{" +
                "id=" + id +
                ", commitBefore='" + commitBefore + '\'' +
                ", commitAfter='" + commitAfter + '\'' +
                ", ref='" + ref + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                ", repositoryName='" + repositoryName + '\'' +
                ", repositoryUrl='" + repositoryUrl + '\'' +
                ", repositoryDescription='" + repositoryDescription + '\'' +
                ", repositoryHomepage='" + repositoryHomepage + '\'' +
                ", totalCommitsCount=" + totalCommitsCount +
                ", triggerBuild=" + triggerBuild +
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

    public String getCommitBefore() {
        return commitBefore;
    }

    public void setCommitBefore(String commitBefore) {
        this.commitBefore = commitBefore;
    }

    public String getCommitAfter() {
        return commitAfter;
    }

    public void setCommitAfter(String commitAfter) {
        this.commitAfter = commitAfter;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getRepositoryDescription() {
        return repositoryDescription;
    }

    public void setRepositoryDescription(String repositoryDescription) {
        this.repositoryDescription = repositoryDescription;
    }

    public String getRepositoryHomepage() {
        return repositoryHomepage;
    }

    public void setRepositoryHomepage(String repositoryHomepage) {
        this.repositoryHomepage = repositoryHomepage;
    }

    public int getTotalCommitsCount() {
        return totalCommitsCount;
    }

    public void setTotalCommitsCount(int totalCommitsCount) {
        this.totalCommitsCount = totalCommitsCount;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public int getWebHooksType() {
        return webHooksType;
    }

    public void setWebHooksType(int webHooksType) {
        this.webHooksType = webHooksType;
    }

    public int getTriggerBuild() {
        return triggerBuild;
    }

    public void setTriggerBuild(int triggerBuild) {
        this.triggerBuild = triggerBuild;
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
