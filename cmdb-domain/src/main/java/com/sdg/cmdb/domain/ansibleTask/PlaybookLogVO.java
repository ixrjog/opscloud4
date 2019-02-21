package com.sdg.cmdb.domain.ansibleTask;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

public class PlaybookLogVO extends PlaybookLogDO implements Serializable {

    private static final long serialVersionUID = 20242078984091402L;

    private UserDO userDO;

    private String playbookLog;

    private String viewTime;

    public PlaybookLogVO() {

    }

    public PlaybookLogVO(PlaybookLogDO playbookLogDO) {
        setId(playbookLogDO.getId());
        setPlaybookId(playbookLogDO.getPlaybookId());
        setPlaybookName(playbookLogDO.getPlaybookName());
        setLogPath(playbookLogDO.getLogPath());
        setContent(playbookLogDO.getContent());
        setDoType(playbookLogDO.getDoType());
        setComplete(playbookLogDO.isComplete());
        setGmtCreate(playbookLogDO.getGmtCreate());
        setGmtModify(playbookLogDO.getGmtModify());
    }

    public PlaybookLogVO(PlaybookLogDO playbookLogDO, UserDO userDO) {
        setId(playbookLogDO.getId());
        setPlaybookId(playbookLogDO.getPlaybookId());
        setPlaybookName(playbookLogDO.getPlaybookName());
        setLogPath(playbookLogDO.getLogPath());
        setContent(playbookLogDO.getContent());
        setDoType(playbookLogDO.getDoType());
        setComplete(playbookLogDO.isComplete());
        setGmtCreate(playbookLogDO.getGmtCreate());
        setGmtModify(playbookLogDO.getGmtModify());
        this.userDO = userDO;
    }

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }

    public String getPlaybookLog() {
        return playbookLog;
    }

    public void setPlaybookLog(String playbookLog) {
        this.playbookLog = playbookLog;
    }

    public String getViewTime() {
        return viewTime;
    }

    public void setViewTime(String viewTime) {
        this.viewTime = viewTime;
    }
}
