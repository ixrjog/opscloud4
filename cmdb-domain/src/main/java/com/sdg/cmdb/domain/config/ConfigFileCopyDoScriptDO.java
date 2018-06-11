package com.sdg.cmdb.domain.config;

import java.io.Serializable;

public class ConfigFileCopyDoScriptDO implements Serializable {
    private long id;

    private long copyId;

    private long taskScriptId;

    private String params;

    private String groupName;

    private String gmtCreate;

    private String gmtModify;


    @Override
    public String toString() {
        return "ConfigFileCopyDoScriptDO{" +
                "id=" + id +
                ", copyId=" + copyId +
                ", taskScriptId=" + taskScriptId +
                ", params='" + params + '\'' +
                ", groupName='" + groupName + '\'' +
                ", copyId=" + copyId +
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

    public long getCopyId() {
        return copyId;
    }

    public void setCopyId(long copyId) {
        this.copyId = copyId;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
