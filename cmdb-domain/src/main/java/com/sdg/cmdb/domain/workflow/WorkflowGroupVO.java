package com.sdg.cmdb.domain.workflow;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工作流视图
 */
@Data
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
