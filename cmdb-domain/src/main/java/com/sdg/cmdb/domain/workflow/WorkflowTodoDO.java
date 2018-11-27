package com.sdg.cmdb.domain.workflow;

import com.sdg.cmdb.domain.auth.UserDO;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class WorkflowTodoDO implements Serializable {
    private static final long serialVersionUID = -49987520988688704L;

    //public static final int TODO_PHASE_CREATE = 0;
    public static final int TODO_PHASE_APPlY = 0;
    //Approval
    public static final int TODO_PHASE_TL_APPROVAL = 1;
    public static final int TODO_PHASE_DL_APPROVAL = 2;
    //  Auditing
    public static final int TODO_PHASE_AUDITING = 3;

    public static final int TODO_PHASE_COMPLETE = 4;

    private long id;

    private long wfId;

    private String wfName;

    private long applyUserId;

    private String applyDisplayName;

    /**
     * 工单状态（工单状态 0:发起人状态  1:团队领导状态  2:部门领导状态  3:审核操作状态  4:执行完成 ）
     */
    private int todoPhase = 0;

    /**
     * 类型  0:正常 1:拒绝回退 2:拒绝 3:正常完成 4:错误
     */
    private int todoStatus = 0;

    /**
     * 发起时间
     */
    private String gmtApply;

    private String gmtCreate;

    private String gmtModify;

    public WorkflowTodoDO(WorkflowDO workflowDO, UserDO applyUser) {
        this.wfId = workflowDO.getId();
        this.wfName = workflowDO.getWfName();
        this.applyUserId = applyUser.getId();
        if (StringUtils.isEmpty(applyUser.getDisplayName())) {
            this.applyDisplayName = applyUser.getUsername();
        } else {
            this.applyDisplayName = applyUser.getUsername() + '<' + applyUser.getDisplayName() + '>';
        }
        this.todoPhase = 0;
        this.todoStatus = 0;
    }

    public WorkflowTodoDO() {

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWfId() {
        return wfId;
    }

    public void setWfId(long wfId) {
        this.wfId = wfId;
    }

    public String getWfName() {
        return wfName;
    }

    public void setWfName(String wfName) {
        this.wfName = wfName;
    }

    public long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyDisplayName() {
        return applyDisplayName;
    }

    public void setApplyDisplayName(String applyDisplayName) {
        this.applyDisplayName = applyDisplayName;
    }

    public int getTodoPhase() {
        return todoPhase;
    }

    public void setTodoPhase(int todoPhase) {
        this.todoPhase = todoPhase;
    }

    public int getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(int todoStatus) {
        this.todoStatus = todoStatus;
    }

    public String getGmtApply() {
        return gmtApply;
    }

    public void setGmtApply(String gmtApply) {
        this.gmtApply = gmtApply;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }
}
