package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

public class TodoPropertyDO implements Serializable {
    private static final long serialVersionUID = -116873129280501911L;

    private long id;

    private long todoDetailId;

    private long todoId;

    private String todoName;

    private String todoKey;

    private String todoValue;

    private String gmtCreate;

    private String gmtModify;

    public TodoPropertyDO() {

    }

    public TodoPropertyDO(long todoDetailId, long todoId, String todoName, String todoKey, String todoValue) {
        this.todoDetailId = todoDetailId;
        this.todoId = todoId;
        this.todoName = todoName;
        this.todoKey = todoKey;
        this.todoValue = todoValue;
    }

    public TodoPropertyDO(TodoDetailVO todoDetailVO, String todoKey, String todoValue) {
        this.todoDetailId = todoDetailVO.getId();
        this.todoId =todoDetailVO.getTodoId();
        this.todoName =todoDetailVO.getTodoDO().getName();
        this.todoKey = todoKey;
        this.todoValue = todoValue;
    }

    public TodoPropertyDO(TodoDetailVO todoDetailVO, String todoKey, boolean todoValue) {
        this.todoDetailId = todoDetailVO.getId();
        this.todoId =todoDetailVO.getTodoId();
        this.todoName =todoDetailVO.getTodoDO().getName();
        this.todoKey = todoKey;
        if(todoValue){
            this.todoValue ="true";
        }else{
            this.todoValue ="false";
        }
    }


    @Override
    public String toString() {
        return "TodoPropertyDO{" +
                "id=" + id +
                ", todoDetailId=" + todoDetailId +
                ", todoId=" + todoId +
                ", todoName='" + todoName + '\'' +
                ", todoKey='" + todoKey + '\'' +
                ", todoValue='" + todoValue + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTodoDetailId() {
        return todoDetailId;
    }

    public void setTodoDetailId(long todoDetailId) {
        this.todoDetailId = todoDetailId;
    }

    public long getTodoId() {
        return todoId;
    }

    public void setTodoId(long todoId) {
        this.todoId = todoId;
    }

    public String getTodoName() {
        return todoName;
    }

    public void setTodoName(String todoName) {
        this.todoName = todoName;
    }

    public String getTodoKey() {
        return todoKey;
    }

    public void setTodoKey(String todoKey) {
        this.todoKey = todoKey;
    }

    public String getTodoValue() {
        return todoValue;
    }

    public void setTodoValue(String todoValue) {
        this.todoValue = todoValue;
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
