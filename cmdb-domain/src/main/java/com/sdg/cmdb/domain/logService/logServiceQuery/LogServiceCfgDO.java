package com.sdg.cmdb.domain.logService.logServiceQuery;

import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceQueryCfg;

import java.io.Serializable;

public class LogServiceCfgDO implements LogServiceQueryCfg, Serializable {
    private static final long serialVersionUID = 7626416951004095434L;

    private long id;

    private String serverName;

    private String content;

    private String project;

    private String logstore;

    private String topic;

    private String gmtCreate;

    private String gmtModify;

    @Override
    public String acqProject(){
        return project;
    }

    @Override
    public String acqLogstore(){
        return logstore;
    }

    @Override
    public  String acqTopic(){
        return topic;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
