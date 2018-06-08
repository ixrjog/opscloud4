package com.sdg.cmdb.domain.gitlab;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class GitlabWebHooksVO implements Serializable {
    private static final long serialVersionUID = -2419670181467361008L;

    public static final String PROJECT_NAME = "name";

    public static final String PROJECT_DESCRIPTION = "description";

    public static final String REPOSITORY_NAME = "name";

    public static final String REPOSITORY_URL = "url";

    public static final String REPOSITORY_DESCRIPTION = "description";

    public static final String REPOSITORY_HOMEPAGE = "homepage";

    private String before;

    // 当前提交的sha值
    private String after;

    private String ref;

    // gitlab userId
    private int userId;

    private String userName;

    // gitlab projectId
    private int projectId;

    private int totalCommitsCount;

    private HashMap<String, String> project;

    private HashMap<String, String> repository;

    private List<CommitsVO> commits;

    @Override
    public String toString() {
        String string = "GitlabWebHooksDO{" +
                " before='" + before + '\'' +
                ", after='" + after + '\'' +
                ", ref='" + ref + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", projectId=" + projectId +
                ", projectName='" + getProjectName() + '\'' +
                ", projectDescription='" + getProjectDescription() + '\'' +
                ", repositoryName='" + getRepositoryName() + '\'' +
                ", repositoryUrl='" + getRepositoryUrl() + '\'' +
                ", repositoryDescription='" + getRepositoryDescription() + '\'' +
                ", repositoryHomepage='" + getRepositoryHomepage() + '\'' +
                ", totalCommitsCount=" + totalCommitsCount +
                ", commits{";


        if (commits != null) {
            for (CommitsVO commitsVO : commits) {
                string += commitsVO.toString();
            }
        } else {
            string += "null";
        }
        string += "}}";
        return string;
    }


    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
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

    public void setUser_id(int userId) {
        this.userId = userId;
    }

    public String getUser_name() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProject_id(int projectId) {
        this.projectId = projectId;
    }


    public HashMap<String, String> getProject() {
        return project;
    }

    public void setProject(HashMap<String, String> project) {
        this.project = project;
    }

    public String getProjectName() {
        if (project == null) return "";
        return (String) project.get(PROJECT_NAME);
    }

    public String getProjectDescription() {
        if (project == null) return "";
        return (String) project.get(PROJECT_DESCRIPTION);
    }

    public String getRepositoryName() {
        if (repository == null) return "";
        return (String) repository.get(REPOSITORY_NAME);
    }


    public String getRepositoryUrl() {
        if (repository == null) return "";
        return (String) repository.get(REPOSITORY_URL);
    }


    public String getRepositoryDescription() {
        if (repository == null) return "";
        return (String) repository.get(REPOSITORY_DESCRIPTION);
    }

    public String getRepositoryHomepage() {
        if (repository == null) return "";
        return (String) repository.get(REPOSITORY_HOMEPAGE);
    }

    public int getTotalCommitsCount() {
        return totalCommitsCount;
    }

    public void setTotal_commits_count(int totalCommitsCount) {
        this.totalCommitsCount = totalCommitsCount;
    }

    public List<CommitsVO> getCommits() {
        return commits;
    }

    public void setCommits(List<CommitsVO> commits) {
        this.commits = commits;
    }


    public HashMap<String, String> getRepository() {
        return repository;
    }

    public void setRepository(HashMap<String, String> repository) {
        this.repository = repository;
    }

    // 提取当前提交的短commit
    public String getCommit() {
        return after.substring(0, 8);
    }


    public enum EnvTypeEnum {
        err(0, "error"),
        tag(1, "prod"),
        // branch
        header(2, "daily");
        private int code;
        private String desc;

        EnvTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getEnvTypeName(int code) {
            for (EnvTypeEnum envTypeEnum : EnvTypeEnum.values()) {
                if (envTypeEnum.getCode() == code) {
                    return envTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    /**
     * 查询用户邮箱
     *
     * @return
     */
    public String acqEmail() {
        if (commits == null) return "";
        for (CommitsVO commit : commits) {
            String email = commit.getAuthorEmail();
            if (!StringUtils.isEmpty(email)) return email;
        }
        return "";
    }

}
