package com.sdg.cmdb.domain.workflow;

import java.io.Serializable;
import java.util.List;

/**
 * 工作流视图
 */
public class WorkflowGroupVO extends WorkflowGroupDO implements Serializable {
    private static final long serialVersionUID = -5243228153219397722L;

    private List<WorkflowDO> workflowList;

    public WorkflowGroupVO(WorkflowGroupDO workflowGroupDO, List<WorkflowDO> workflowList) {
        setGroupName(workflowGroupDO.getGroupName());
        setContent(workflowGroupDO.getContent());
        this.workflowList = workflowList;
    }

    public WorkflowGroupVO() {

    }


    public List<WorkflowDO> getWorkflowList() {
        return workflowList;
    }

    public void setWorkflowList(List<WorkflowDO> workflowList) {
        this.workflowList = workflowList;
    }
}
