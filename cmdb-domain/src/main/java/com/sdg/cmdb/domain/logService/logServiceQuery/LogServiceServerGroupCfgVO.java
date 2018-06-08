package com.sdg.cmdb.domain.logService.logServiceQuery;

import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;

public class LogServiceServerGroupCfgVO extends LogServiceServerGroupCfgDO implements Serializable {
    private static final long serialVersionUID = 573733901286113356L;

    private boolean authed = false;

    private ServerGroupDO serverGroupDO;


    public boolean isAuthed() {
        return authed;
    }

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }

    public void setAuthed(boolean authed) {
        this.authed = authed;
    }


    public LogServiceServerGroupCfgVO(){

    }
    public LogServiceServerGroupCfgVO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;

    }

    public LogServiceServerGroupCfgVO(ServerGroupDO serverGroupDO, LogServiceServerGroupCfgDO logServiceServerGroupCfgDO, boolean authed) {
        this.authed = authed;
        this.serverGroupDO = serverGroupDO;
        setId(logServiceServerGroupCfgDO.getId());
        setServerGroupId(logServiceServerGroupCfgDO.getServerGroupId());
        setServerGroupName(logServiceServerGroupCfgDO.getServerGroupName());
        setProject(logServiceServerGroupCfgDO.getProject());
        setLogstore(logServiceServerGroupCfgDO.getLogstore());
        setTopic(logServiceServerGroupCfgDO.getTopic());
        setGmtCreate(logServiceServerGroupCfgDO.getGmtCreate());
        setGmtModify(logServiceServerGroupCfgDO.getGmtModify());
    }

}
