package com.sdg.cmdb.domain.ansibleTask;

import java.io.Serializable;

public class TaskScriptDO implements Serializable {

    private static final long serialVersionUID = -4555277124479409664L;

    private long id;

    private String scriptName;

    private String content;

    private long userId;

    private String username;

    private String script;

    // 脚本类型0:私有 1:公开
    private int scriptType = 0;

    // 1系统脚本
    private int sysScript = 0;

    private String gmtCreate;

    private String gmtModify;

    public TaskScriptDO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public int getScriptType() {
        return scriptType;
    }

    public void setScriptType(int scriptType) {
        this.scriptType = scriptType;
    }

    public int getSysScript() {
        return sysScript;
    }

    public void setSysScript(int sysScript) {
        this.sysScript = sysScript;
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
