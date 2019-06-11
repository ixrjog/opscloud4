package com.sdg.cmdb.domain.workflow;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailVO;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Data
public class WorkflowTodoVO extends WorkflowTodoDO implements Serializable {

    private static final long serialVersionUID = -7525786606283551913L;

    private String applyViewTime;
    private List<WorkflowTodoDetailVO> todoDetails;
    private HashMap<String, WorkflowTodoUserDO> todoUserList;
    private List<UserVO> qaUserList;  // 质量保证用户列表
    private List<UserVO> cmoUserList; // 配置管理员用户列表
    private List<UserVO> dlUserList;  // 部门经理用户列表
    private List<UserVO> opsUserList; // 运维工程师用户列表
    private WorkflowDO workflowDO;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public WorkflowTodoVO(WorkflowDO workflowDO, WorkflowTodoDO workflowTodoDO, List<WorkflowTodoDetailVO> todoDetails, HashMap<String, WorkflowTodoUserDO> todoUserList) {
        this.workflowDO = workflowDO;
        setId(workflowTodoDO.getId());
        setWfId(workflowTodoDO.getWfId());
        setWfName(workflowTodoDO.getWfName());
        setApplyUserId(workflowTodoDO.getApplyUserId());
        setApplyDisplayName(workflowTodoDO.getApplyDisplayName());
        setTodoPhase(workflowTodoDO.getTodoPhase());
        setTodoStatus(workflowTodoDO.getTodoStatus());
        setGmtApply(workflowTodoDO.getGmtApply());
        setContent(workflowTodoDO.getContent());
        this.todoDetails = todoDetails;
        this.todoUserList = todoUserList;
        setNotice(workflowTodoDO.getNotice());
    }

    public WorkflowTodoVO() {
    }
}
