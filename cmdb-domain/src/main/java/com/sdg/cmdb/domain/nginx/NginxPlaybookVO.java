package com.sdg.cmdb.domain.nginx;

import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;

public class NginxPlaybookVO extends NginxPlaybookDO {

    public NginxPlaybookVO(NginxPlaybookDO nginxPlaybookDO,VhostVO vhostVO ,TaskScriptDO taskScriptDO) {
        setId(nginxPlaybookDO.getId());
        setVhostId(nginxPlaybookDO.getVhostId());
        setServerKey(getServerKey());
        setServerGroupId(nginxPlaybookDO.getServerGroupId());
        setServerGroupName(nginxPlaybookDO.getServerGroupName());
        setHostPattern(nginxPlaybookDO.getHostPattern());
        setSrc(nginxPlaybookDO.getSrc());
        setDest(nginxPlaybookDO.getDest());
        setUsername(nginxPlaybookDO.getUsername());
        setUsergroup(nginxPlaybookDO.getUsergroup());
        setPlaybookId(nginxPlaybookDO.getPlaybookId());
        setGmtCreate(nginxPlaybookDO.getGmtCreate());
        setGmtModify(nginxPlaybookDO.getGmtModify());
        this.vhostVO = vhostVO;
        this.taskScriptDO = taskScriptDO;
    }

    private VhostVO vhostVO;

    public NginxPlaybookVO() {

    }

    private Object groupHostPattern;

    public Object getGroupHostPattern() {
        return groupHostPattern;
    }

    public void setGroupHostPattern(Object groupHostPattern) {
        this.groupHostPattern = groupHostPattern;
    }

    private ServerGroupDO serverGroupDO;


    private TaskScriptDO taskScriptDO;

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }


    public TaskScriptDO getTaskScriptDO() {
        return taskScriptDO;
    }

    public void setTaskScriptDO(TaskScriptDO taskScriptDO) {
        this.taskScriptDO = taskScriptDO;
    }

    public VhostVO getVhostVO() {
        return vhostVO;
    }

    public void setVhostVO(VhostVO vhostVO) {
        this.vhostVO = vhostVO;
    }
}
