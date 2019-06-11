package com.sdg.cmdb.domain.workflow;

import lombok.Data;

import java.io.Serializable;

@Data
public class WorkflowVO extends WorkflowDO implements Serializable {
    private static final long serialVersionUID = 2288172905163524843L;

    public WorkflowVO(WorkflowDO workflowDO){
        setId(workflowDO.getId());
        setGroupId(workflowDO.getGroupId());
        setWfName(workflowDO.getWfName());
        setWfKey(workflowDO.getWfKey());
        setWfStatus(workflowDO.getWfStatus());
        setTitle(workflowDO.getTitle());
        setContent(workflowDO.getContent());
        setHelpUrl(workflowDO.getHelpUrl());
        setTopics(workflowDO.getTopics());
        setWfType(workflowDO.getWfType());
        setApproval(workflowDO.isApproval());
        setGmtCreate(workflowDO.getGmtCreate());
        setGmtModify(workflowDO.getGmtModify());
    }

    public WorkflowVO(){

    }

}
