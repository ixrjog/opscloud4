package com.sdg.cmdb.domain.ansibleTask;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.config.ConfigFilePlaybookVO;
import com.sdg.cmdb.domain.nginx.NginxPlaybookVO;

import java.io.Serializable;

public class PlaybookLogDO implements Serializable {

    private static final long serialVersionUID = -2541376826735255627L;

    private long id;

    private long playbookId;

    private String playbookName;

    private String logPath;

    private String content;

    /**
     * 0 系统执行 1 人工操作
     */
    private int doType = 0;

    private long userId;

    private String displayName;

    private boolean complete = false;

    private String gmtCreate;

    private String gmtModify;


    public PlaybookLogDO(TaskScriptDO taskScriptDO, UserDO userDO) {
        this.playbookId = taskScriptDO.getId();
        this.playbookName = taskScriptDO.getScriptName();
        this.userId = userDO.getId();
        this.displayName = userDO.getDisplayName();
        this.doType = 1;
    }

    public PlaybookLogDO(ConfigFilePlaybookVO configFilePlaybookVO) {
        this.playbookId = configFilePlaybookVO.getPlaybookId();
        this.playbookName = configFilePlaybookVO.getTaskScriptDO().getScriptName();
    }

    public PlaybookLogDO(NginxPlaybookVO nginxPlaybookVO) {
        this.playbookId = nginxPlaybookVO.getPlaybookId();
        this.playbookName = nginxPlaybookVO.getTaskScriptDO().getScriptName();
    }

    public PlaybookLogDO(ConfigFilePlaybookVO configFilePlaybookVO, UserDO userDO) {
        this.playbookId = configFilePlaybookVO.getPlaybookId();
        this.playbookName = configFilePlaybookVO.getTaskScriptDO().getScriptName();
        this.doType = 1;
        this.userId = userDO.getId();
        this.displayName = userDO.getUsername() + "<" + userDO.getDisplayName() + ">";
    }


    public PlaybookLogDO(NginxPlaybookVO nginxPlaybookVO, UserDO userDO) {
        this.playbookId = nginxPlaybookVO.getPlaybookId();
        this.playbookName = nginxPlaybookVO.getTaskScriptDO().getScriptName();
        this.doType = 1;
        this.userId = userDO.getId();
        this.displayName = userDO.getUsername() + "<" + userDO.getDisplayName() + ">";
    }


    public PlaybookLogDO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlaybookId() {
        return playbookId;
    }

    public void setPlaybookId(long playbookId) {
        this.playbookId = playbookId;
    }

    public String getPlaybookName() {
        return playbookName;
    }

    public void setPlaybookName(String playbookName) {
        this.playbookName = playbookName;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDoType() {
        return doType;
    }

    public void setDoType(int doType) {
        this.doType = doType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
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
