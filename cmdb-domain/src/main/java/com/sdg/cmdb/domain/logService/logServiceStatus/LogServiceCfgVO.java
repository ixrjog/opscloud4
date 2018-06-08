package com.sdg.cmdb.domain.logService.logServiceStatus;

import java.io.Serializable;

public class LogServiceCfgVO implements Serializable {
    private static final long serialVersionUID = 5915480968644940676L;

    // 日志服务的服务器组数量
    private int serverGroupCfgCnt = 0;

    // 日志服务的服务器数量
    private int serverCnt = 0;


    public LogServiceCfgVO() {

    }

    public LogServiceCfgVO(int serverGroupCfgCnt, int serverCnt) {
        this.serverGroupCfgCnt = serverGroupCfgCnt;
        this.serverCnt = serverCnt;

    }

    public int getServerGroupCfgCnt() {
        return serverGroupCfgCnt;
    }

    public void setServerGroupCfgCnt(int serverGroupCfgCnt) {
        this.serverGroupCfgCnt = serverGroupCfgCnt;
    }

    public int getServerCnt() {
        return serverCnt;
    }

    public void setServerCnt(int serverCnt) {
        this.serverCnt = serverCnt;
    }
}
