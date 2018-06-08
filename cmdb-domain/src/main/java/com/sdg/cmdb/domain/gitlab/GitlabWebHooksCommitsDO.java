package com.sdg.cmdb.domain.gitlab;

import java.io.Serializable;

public class GitlabWebHooksCommitsDO implements Serializable {
    private static final long serialVersionUID = 5579113541186154096L;

    private long id;

    private long webHooksId;
    private String commitsId;

    private String message;

    private String timestamp;

    private String url;

    private String autherName;

    private String autherEmail;

    private String gmtCreate;

    private String gmtModify;

    public GitlabWebHooksCommitsDO() {

    }

    public GitlabWebHooksCommitsDO(CommitsVO commitsVO, long webHooksId) {
        this.commitsId = commitsVO.getId();
        this.webHooksId = webHooksId;
        this.message = commitsVO.getMessage();
        this.timestamp = commitsVO.getTimestamp();
        this.url = commitsVO.getUrl();
        this.autherName = commitsVO.getAuthorName();
        this.autherEmail = commitsVO.getAuthorEmail();
    }

    @Override
    public String toString() {
        return "GitlabWebHooksCommitsDO{" +
                "id=" + id +
                ", commitsId='" + commitsId + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", url=" + url +
                ", autherName='" + autherName + '\'' +
                ", autherEmail=" + autherEmail +
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

    public String getCommitsId() {
        return commitsId;
    }

    public void setCommitsId(String commitsId) {
        this.commitsId = commitsId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAutherName() {
        return autherName;
    }

    public void setAutherName(String autherName) {
        this.autherName = autherName;
    }

    public String getAutherEmail() {
        return autherEmail;
    }

    public void setAutherEmail(String autherEmail) {
        this.autherEmail = autherEmail;
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
