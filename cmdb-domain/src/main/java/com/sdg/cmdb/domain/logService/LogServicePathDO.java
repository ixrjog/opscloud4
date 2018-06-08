package com.sdg.cmdb.domain.logService;

import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceDefaultQuery;

import java.io.Serializable;

public class LogServicePathDO implements Serializable {
    private static final long serialVersionUID = 7264026046548936297L;

    private long id;

    private long serverGroupId;

    // 配置的日志搜素目录
    private String logDir;

    private String tagPath;

    private int searchCnt = 1;

    private String gmtCreate;

    private String gmtModify;


    @Override
    public String toString() {
        return "LogServicePathDO{" +
                "id=" + id +
                ", serverGroupId=" + serverGroupId +
                ", logDir='" + logDir + '\'' +
                ", tagPath='" + tagPath + '\'' +
                ", searchCnt=" + searchCnt +
                ", gmtCreate='" + gmtCreate + '\'' +
                ",  gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public LogServicePathDO() {

    }

    public LogServicePathDO(String tagPath, long serverGroupId) {
        this.tagPath = tagPath;
        this.serverGroupId = serverGroupId;
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

    public String getLogDir() {
        return logDir;
    }

    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }

    public String getTagPath() {
        return tagPath;
    }

    public void setTagPath(String tagPath) {
        this.tagPath = tagPath;
    }

    public int getSearchCnt() {
        return searchCnt;
    }

    public void setSearchCnt(int searchCnt) {
        this.searchCnt = searchCnt;
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
