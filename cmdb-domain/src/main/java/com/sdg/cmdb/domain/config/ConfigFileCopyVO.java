package com.sdg.cmdb.domain.config;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerVO;

import java.io.Serializable;

public class ConfigFileCopyVO extends ConfigFileCopyDO implements Serializable {


    public ConfigFileCopyVO() {

    }

    public ConfigFileCopyVO(ConfigFileCopyDO configFileCopyDO) {
        setId(configFileCopyDO.getId());
        setGroupId(configFileCopyDO.getGroupId());
        setGroupName(configFileCopyDO.getGroupName());
        setServerId(configFileCopyDO.getServerId());
        setSrc(configFileCopyDO.getSrc());
        setDest(configFileCopyDO.getDest());
        setUsername(configFileCopyDO.getUsername());
        setUsergroup(configFileCopyDO.getUsergroup());
        setGmtCreate(configFileCopyDO.getGmtCreate());
        setGmtModify(configFileCopyDO.getGmtModify());
    }

    private ConfigFileGroupDO configFileGroupDO;


    private ServerDO serverDO;

    private ServerGroupDO serverGroupDO;


    public ServerDO getServerDO() {
        return serverDO;
    }

    public void setServerDO(ServerDO serverDO) {
        this.serverDO = serverDO;
    }

    public ConfigFileGroupDO getConfigFileGroupDO() {
        return configFileGroupDO;
    }

    public void setConfigFileGroupDO(ConfigFileGroupDO configFileGroupDO) {
        this.configFileGroupDO = configFileGroupDO;
    }

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }
}
