package com.sdg.cmdb.domain.copy;

import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import com.sdg.cmdb.domain.nginx.VhostEnvDO;
import com.sdg.cmdb.domain.nginx.VhostVO;

import java.io.Serializable;
import java.util.List;

public class CopyVO extends CopyDO implements Serializable {
    private static final long serialVersionUID = 331949755884934622L;

    private VhostVO vhostVO;

    private VhostEnvDO vhostEnvDO;

    private TaskScriptDO taskScriptDO;

    private List<CopyServerVO> copyServerList;

    public CopyVO() {
    }

    public CopyVO(CopyDO copyDO) {
        setId(copyDO.getId());
        setBusinessKey(copyDO.getBusinessKey());
        setBusinessId(copyDO.getBusinessId());
        setSrcPath(copyDO.getSrcPath());
        setDestPath(copyDO.getDestPath());
        setUsername(copyDO.getUsername());
        setUsergroup(copyDO.getUsergroup());

        setDoCopy(copyDO.isDoCopy());
        setDoScript(copyDO.isDoScript());
        setTaskScriptId(copyDO.getTaskScriptId());
        setParams(copyDO.getParams());

        setContent(copyDO.getContent());
        setGmtCreate(copyDO.getGmtCreate());
        setGmtModify(copyDO.getGmtModify());
    }

    public VhostVO getVhostVO() {
        return vhostVO;
    }

    public void setVhostVO(VhostVO vhostVO) {
        this.vhostVO = vhostVO;
    }

    public VhostEnvDO getVhostEnvDO() {
        return vhostEnvDO;
    }

    public void setVhostEnvDO(VhostEnvDO vhostEnvDO) {
        this.vhostEnvDO = vhostEnvDO;
    }

    public TaskScriptDO getTaskScriptDO() {
        return taskScriptDO;
    }

    public void setTaskScriptDO(TaskScriptDO taskScriptDO) {
        this.taskScriptDO = taskScriptDO;
    }

    public List<CopyServerVO> getCopyServerList() {
        return copyServerList;
    }

    public void setCopyServerList(List<CopyServerVO> copyServerList) {
        this.copyServerList = copyServerList;
    }
}
