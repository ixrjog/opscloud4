package com.sdg.cmdb.domain.logService.logServiceQuery;

import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceQueryCfg;

import java.io.Serializable;

public class LogServiceServerGroupCfgDO implements LogServiceQueryCfg,Serializable {
    private static final long serialVersionUID = -7826437605461497612L;

    private long id;

    private long serverGroupId;

    private String serverGroupName;

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

    public long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(long serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public String getServerGroupName() {
        return serverGroupName;
    }

    public void setServerGroupName(String serverGroupName) {
        this.serverGroupName = serverGroupName;
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
