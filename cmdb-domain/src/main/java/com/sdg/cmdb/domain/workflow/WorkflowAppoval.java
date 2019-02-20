package com.sdg.cmdb.domain.workflow;

import java.io.Serializable;

/**
 * 审核对象
 */
public class WorkflowAppoval implements Serializable {

    private static final long serialVersionUID = 1959237551445346720L;

    /**
     * 校验 是否完成
     */
    private boolean check = false;

    private boolean appoval;

    private WorkflowTodoUserDO todoUser;

    public WorkflowAppoval() {

    }

    public WorkflowAppoval(boolean appoval, WorkflowTodoUserDO todoUser) {
        this.appoval = appoval;
        this.todoUser = todoUser;
    }

    public WorkflowTodoUserDO getTodoUser() {
        return todoUser;
    }

    public void setTodoUser(WorkflowTodoUserDO todoUser) {
        this.todoUser = todoUser;
    }


    public boolean isCheck() {
        if (appoval) {
            if (this.todoUser == null) return false;
            if (todoUser.getEvaluation() == WorkflowTodoUserDO.EvaluationTypeEnum.approve.getCode()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isAppoval() {
        return appoval;
    }

    public void setAppoval(boolean appoval) {
        this.appoval = appoval;
    }
}
