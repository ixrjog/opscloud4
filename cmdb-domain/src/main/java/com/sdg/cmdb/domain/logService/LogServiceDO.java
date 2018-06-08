package com.sdg.cmdb.domain.logService;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceQuery;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceQueryCfg;

import java.io.Serializable;

public class LogServiceDO implements Serializable {
    private static final long serialVersionUID = -6797093347636974112L;

    private long id;

    private long userId;

    private String username;

    private String project;

    private String logstore;

    private String topic;

    private String query;

    private int timeFrom;

    private int timeTo;

    // 日志条目
    private int totalCount;

    private String gmtCreate;

    private String gmtModify;

    public LogServiceDO(){

    }

    public LogServiceDO(LogServiceQueryCfg cfg, UserDO userDO, String query, int timeFrom, int timeTo) {
        this.userId = userDO.getId();
        this.username = userDO.getUsername();
       // LogServiceCfgDO logServiceCfgDO = logServiceKaQuery.getLogServiceCfg();
        this.project = cfg.acqProject();
        this.logstore = cfg.acqLogstore();
        this.topic = cfg.acqTopic();
        this.query = query;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getLogstore() {
        return logstore;
    }

    public void setLogstore(String logstore) {
        this.logstore = logstore;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(int timeFrom) {
        this.timeFrom = timeFrom;
    }

    public int getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(int timeTo) {
        this.timeTo = timeTo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
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
