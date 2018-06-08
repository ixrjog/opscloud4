package com.sdg.cmdb.domain.gitlab;

import java.io.Serializable;

public class RepositoryDO implements Serializable {

    private static final long serialVersionUID = 6769472869012844857L;
    private long id;

    private String repositoryUrl;

    private int repositoryType;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    public RepositoryDO(){

    }

    public RepositoryDO(String repositoryUrl,int repositoryType){
        this.repositoryUrl =repositoryUrl;
        this.repositoryType = repositoryType;
    }

    @Override
    public String toString() {
        return "RepositoryDO{" +
                "id=" + id +
                ", repositoryUrl='" + repositoryUrl + '\'' +
                ", repositoryType=" + repositoryType +
                ", content='" + content + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    /**
     * repositoryType
     */
    public enum RepoTypeEnum {
        gitlab(0, "gitlab"),
        stash(1, "stash");

        private int code;
        private String desc;

        RepoTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getRepoTypeName(int code) {
            for (RepoTypeEnum repoTypeEnum : RepoTypeEnum.values()) {
                if (repoTypeEnum.getCode() == code) {
                    return repoTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public int getRepositoryType() {
        return repositoryType;
    }

    public void setRepositoryType(int repositoryType) {
        this.repositoryType = repositoryType;
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
}
