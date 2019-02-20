package com.sdg.cmdb.domain.ci;

import com.alibaba.fastjson.JSON;
import org.gitlab.api.models.GitlabCommit;

import java.io.Serializable;

public class CiBuildCommitDO implements Serializable {
    private static final long serialVersionUID = -1088621910139373893L;

    private long id;

    private long buildId;

    private String commit;

    private String message;

    private String gmtCreate;

    private String gmtModify;

    public CiBuildCommitDO(){}

    public CiBuildCommitDO(long buildId, GitlabCommit gitlabCommit){
        this.buildId = buildId;
        this.commit = gitlabCommit.getId();
        this.message = gitlabCommit.getMessage();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBuildId() {
        return buildId;
    }

    public void setBuildId(long buildId) {
        this.buildId = buildId;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
