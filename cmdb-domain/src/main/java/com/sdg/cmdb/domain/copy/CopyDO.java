package com.sdg.cmdb.domain.copy;

import java.io.Serializable;

public class CopyDO implements Serializable {
    private static final long serialVersionUID = -4489920612486827259L;

    private long id;

    private String businessKey;

    private long businessId;

    private String srcPath;

    private String destPath;

    private String username;

    private String usergroup;

    private boolean doCopy = true;

    private boolean doScript = false;

    private long taskScriptId;

    private String params;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    public CopyDO() {

    }

    @Override
    public String toString() {
        return "CopyDO{" +
                "id=" + id +
                ", businessKey='" + businessKey + '\'' +
                ", businessId=" + businessId +
                ", srcPath='" + srcPath + '\'' +
                ", destPath='" + destPath + '\'' +
                ", username='" + username + '\'' +
                ", usergroup='" + usergroup + '\'' +
                ", doCopy='" + doCopy + '\'' +
                ", doScript='" + doScript + '\'' +
                ", taskScriptId=" + taskScriptId  +
                ", params='" + params + '\'' +
                ", content='" + content + '\'' +
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

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup;
    }

    public boolean isDoCopy() {
        return doCopy;
    }

    public void setDoCopy(boolean doCopy) {
        this.doCopy = doCopy;
    }

    public boolean isDoScript() {
        return doScript;
    }

    public void setDoScript(boolean doScript) {
        this.doScript = doScript;
    }

    public long getTaskScriptId() {
        return taskScriptId;
    }

    public void setTaskScriptId(long taskScriptId) {
        this.taskScriptId = taskScriptId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
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
