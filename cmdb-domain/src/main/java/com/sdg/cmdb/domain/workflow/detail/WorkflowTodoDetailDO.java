package com.sdg.cmdb.domain.workflow.detail;

import java.io.Serializable;

public class WorkflowTodoDetailDO implements Serializable {

    private static final long serialVersionUID = 4711830495842147617L;

    private long id;

    private long todoId;

    private String name;

    private String detailKey;

    private String detailValue;

    static public final int STATUS_NOT = 0;

    static public final int STATUS_COMPLETE = 1;

    static public final int STATUS_ERR = 2;


    /**
     * 处理状态 0 未处理，1 处理完成  2 ERR
     */
    private int detailStatus = 0;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    public WorkflowTodoDetailDO(long todoId, String name, String detailKey) {
        this.todoId = todoId;
        this.name = name;
        this.detailKey = detailKey;
    }

    public WorkflowTodoDetailDO(WorkflowTodoDetailVO workflowTodoDetailVO, String detailValue) {
        this.id = workflowTodoDetailVO.getId();
        this.todoId = workflowTodoDetailVO.getTodoId();
        this.name = workflowTodoDetailVO.getName();
        this.detailKey = workflowTodoDetailVO.getDetailKey();
        this.detailValue = detailValue;
        this.content = workflowTodoDetailVO.getContent();
    }

    public WorkflowTodoDetailDO() {

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTodoId() {
        return todoId;
    }

    public void setTodoId(long todoId) {
        this.todoId = todoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailKey() {
        return detailKey;
    }

    public void setDetailKey(String detailKey) {
        this.detailKey = detailKey;
    }

    public String getDetailValue() {
        return detailValue;
    }

    public void setDetailValue(String detailValue) {
        this.detailValue = detailValue;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(int detailStatus) {
        this.detailStatus = detailStatus;
    }
}
