package com.sdg.cmdb.domain.workflow;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailVO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class WorkflowTodoVO extends WorkflowTodoDO implements Serializable {

    private static final long serialVersionUID = -7525786606283551913L;

    private String applyViewTime;

    private List<WorkflowTodoDetailVO> todoDetails;

    private HashMap<String, WorkflowTodoUserDO> todoUserList;

    // 质量保证用户列表
    private List<UserVO> qaUserList;

    // 部门经理用户列表
    private List<UserVO> dlUserList;

    // 运维工程师用户列表
    private List<UserVO> opsUserList;

    private WorkflowDO workflowDO;

    @Override
    public String toString() {
        return "WorkflowTodoVO{" +
                "id=" + getId() +
                ", wfId=" + getWfId() +
                ", wfName='" + getWfName() + '\'' +
                ", applyUserId=" + getApplyUserId() +
                ", applyDisplayName='" + getApplyDisplayName() + '\'' +
                ", todoPhase=" + getTodoPhase() +
                ", todoStatus=" + getTodoStatus() +
                ", gmtApply='" + getGmtApply() + '\'' +
                ", applyViewTime='" + applyViewTime + '\'' +
                ", content='" + getContent() + '\'' +
                ", gmtModify='" + getGmtModify() + '\'' +
                ", gmtCreate='" + getGmtCreate() + '\'' +
                '}';
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

    public String getApplyViewTime() {
        return applyViewTime;
    }

    public void setApplyViewTime(String applyViewTime) {
        this.applyViewTime = applyViewTime;
    }

    public HashMap<String, WorkflowTodoUserDO> getTodoUserList() {
        return todoUserList;
    }

    public void setTodoUserList(HashMap<String, WorkflowTodoUserDO> todoUserList) {
        this.todoUserList = todoUserList;
    }

    public WorkflowDO getWorkflowDO() {
        return workflowDO;
    }

    public void setWorkflowDO(WorkflowDO workflowDO) {
        this.workflowDO = workflowDO;
    }

    public List<WorkflowTodoDetailVO> getTodoDetails() {
        return todoDetails;
    }

    public void setTodoDetails(List<WorkflowTodoDetailVO> todoDetails) {
        this.todoDetails = todoDetails;
    }

    public List<UserVO> getQaUserList() {
        return qaUserList;
    }

    public void setQaUserList(List<UserVO> qaUserList) {
        this.qaUserList = qaUserList;
    }

    public List<UserVO> getDlUserList() {
        return dlUserList;
    }

    public void setDlUserList(List<UserVO> dlUserList) {
        this.dlUserList = dlUserList;
    }

    public List<UserVO> getOpsUserList() {
        return opsUserList;
    }

    public void setOpsUserList(List<UserVO> opsUserList) {
        this.opsUserList = opsUserList;
    }
}
