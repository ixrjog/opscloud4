package com.sdg.cmdb.domain.ansibleTask;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

public class TaskScriptVO extends TaskScriptDO implements Serializable {
    private static final long serialVersionUID = -6586785352248913631L;

    private UserDO userDO;

    public TaskScriptVO() {

    }

    public TaskScriptVO(TaskScriptDO taskScriptDO, UserDO userDO) {
        this.userDO = userDO;
        setId(taskScriptDO.getId());
        setScriptName(taskScriptDO.getScriptName());
        setContent(taskScriptDO.getContent());
        setUserId(taskScriptDO.getUserId());
        setUsername(taskScriptDO.getUsername());
        setScript(taskScriptDO.getScript());
        setScriptType(taskScriptDO.getScriptType());
        setSysScript(taskScriptDO.getSysScript());
        setGmtCreate(taskScriptDO.getGmtCreate());
        setGmtModify(taskScriptDO.getGmtModify());
    }

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }
}
