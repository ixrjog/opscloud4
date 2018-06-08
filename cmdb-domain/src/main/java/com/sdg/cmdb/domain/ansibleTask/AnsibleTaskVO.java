package com.sdg.cmdb.domain.ansibleTask;

import java.io.Serializable;
import java.util.List;

public class AnsibleTaskVO implements Serializable {


    public AnsibleTaskVO() {

    }

    public AnsibleTaskVO(AnsibleTaskDO ansibleTaskDO) {
        this.ansibleTaskDO = ansibleTaskDO;
    }

    public AnsibleTaskVO(AnsibleTaskDO ansibleTaskDO, List<AnsibleTaskServerVO> taskServerList) {
        this.ansibleTaskDO = ansibleTaskDO;
        this.taskServerList = taskServerList;
    }

    private static final long serialVersionUID = -5639862800902919847L;

    private AnsibleTaskDO ansibleTaskDO;

    private List<AnsibleTaskServerVO> taskServerList;


    private TaskScriptDO taskScriptDO;

    public AnsibleTaskDO getAnsibleTaskDO() {
        return ansibleTaskDO;
    }

    public void setAnsibleTaskDO(AnsibleTaskDO ansibleTaskDO) {
        this.ansibleTaskDO = ansibleTaskDO;
    }

    public List<AnsibleTaskServerVO> getTaskServerList() {
        return taskServerList;
    }

    public void setTaskServerList(List<AnsibleTaskServerVO> taskServerList) {
        this.taskServerList = taskServerList;
    }

    public TaskScriptDO getTaskScriptDO() {
        return taskScriptDO;
    }

    public void setTaskScriptDO(TaskScriptDO taskScriptDO) {
        this.taskScriptDO = taskScriptDO;
    }
}
