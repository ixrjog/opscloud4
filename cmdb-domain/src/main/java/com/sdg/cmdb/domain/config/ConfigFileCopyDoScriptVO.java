package com.sdg.cmdb.domain.config;

import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

public class ConfigFileCopyDoScriptVO extends ConfigFileCopyDoScriptDO implements Serializable {


//
//    private long id;
//
//    private long copyId;
//
//    private long taskScriptId;
//
//    private String params;
//
//    private String groupName;
//
//    private String gmtCreate;
//
//    private String gmtModify;

    private ServerDO serverDO;

    private TaskScriptDO taskScriptDO;

    public ConfigFileCopyDoScriptVO(ConfigFileCopyDoScriptDO doScriptDO, ServerDO serverDO, TaskScriptDO taskScriptDO) {
        setId(doScriptDO.getId());
        setCopyId(doScriptDO.getCopyId());
        setTaskScriptId(doScriptDO.getTaskScriptId());
        setParams(doScriptDO.getParams());
        setGroupName(doScriptDO.getGroupName());
        setGmtCreate(doScriptDO.getGmtCreate());
        setGmtModify(doScriptDO.getGmtModify());
        this.serverDO = serverDO;
        this.taskScriptDO = taskScriptDO;
    }

    public ConfigFileCopyDoScriptVO() {

    }

    public ServerDO getServerDO() {
        return serverDO;
    }

    public void setServerDO(ServerDO serverDO) {
        this.serverDO = serverDO;
    }

    public TaskScriptDO getTaskScriptDO() {
        return taskScriptDO;
    }

    public void setTaskScriptDO(TaskScriptDO taskScriptDO) {
        this.taskScriptDO = taskScriptDO;
    }
}
