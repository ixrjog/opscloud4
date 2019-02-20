package com.sdg.cmdb.domain.workflow.detail;



import java.io.Serializable;

public class WorkflowTodoDetailVO<T> extends WorkflowTodoDetailDO implements Serializable {

    private static final long serialVersionUID = 6337968528734914770L;

    private T detail;

    public T getDetail() {
        return detail;
    }

    public void setDetail(T detail) {
        this.detail = detail;
    }

    public WorkflowTodoDetailVO() {

    }

    public WorkflowTodoDetailVO(T detail, WorkflowTodoDetailDO workflowTodoDetailDO) {
        this.detail = detail;
        setId(workflowTodoDetailDO.getId());
        setTodoId(workflowTodoDetailDO.getTodoId());
        setName(workflowTodoDetailDO.getName());
        setDetailKey(workflowTodoDetailDO.getDetailKey());
        setDetailStatus(workflowTodoDetailDO.getDetailStatus());
        setContent(workflowTodoDetailDO.getContent());
    }


}
