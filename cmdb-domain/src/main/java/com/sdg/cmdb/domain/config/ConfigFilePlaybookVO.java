package com.sdg.cmdb.domain.config;

import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;

public class ConfigFilePlaybookVO extends ConfigFilePlaybookDO implements Serializable {
    private static final long serialVersionUID = 9098358688072985954L;


    public ConfigFilePlaybookVO(ConfigFilePlaybookDO configFilePlaybookDO) {
        setId(configFilePlaybookDO.getId());
        setFileId(configFilePlaybookDO.getFileId());
        setFileName(configFilePlaybookDO.getFileName());
        setServerGroupId(configFilePlaybookDO.getServerGroupId());
        setServerGroupName(configFilePlaybookDO.getServerGroupName());
        setHostPattern(configFilePlaybookDO.getHostPattern());
        setSrc(configFilePlaybookDO.getSrc());
        setDest(configFilePlaybookDO.getDest());
        setUsername(configFilePlaybookDO.getUsername());
        setUsergroup(configFilePlaybookDO.getUsergroup());
        setPlaybookId(configFilePlaybookDO.getPlaybookId());
        setGmtCreate(configFilePlaybookDO.getGmtCreate());
        setGmtModify(configFilePlaybookDO.getGmtModify());
    }

    public ConfigFilePlaybookVO() {

    }

    private Object groupHostPattern;

    public Object getGroupHostPattern() {
        return groupHostPattern;
    }

    public void setGroupHostPattern(Object groupHostPattern) {
        this.groupHostPattern = groupHostPattern;
    }

    private ServerGroupDO serverGroupDO;

    private ConfigFileDO configFileDO;

    private TaskScriptDO taskScriptDO;


    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }

    public ConfigFileDO getConfigFileDO() {
        return configFileDO;
    }

    public void setConfigFileDO(ConfigFileDO configFileDO) {
        this.configFileDO = configFileDO;
    }

    public TaskScriptDO getTaskScriptDO() {
        return taskScriptDO;
    }

    public void setTaskScriptDO(TaskScriptDO taskScriptDO) {
        this.taskScriptDO = taskScriptDO;
    }

}
