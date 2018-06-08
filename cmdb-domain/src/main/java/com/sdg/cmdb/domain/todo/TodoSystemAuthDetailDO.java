package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/9/13.
 */
public class TodoSystemAuthDetailDO implements Serializable {
    private static final long serialVersionUID = 4589087826809714661L;

    private long id;

    private long todoDetailId;

    private String name;

    /**
     * 0 LDAP
     * 1 GETWAY
     */
    private int systemType;

    private String content;

    private boolean authed = false;

    private boolean need;

    private int processStatus;

    private String gmtCreate;

    private String gmtModify;

    public TodoSystemAuthDetailDO() {

    }

    public TodoSystemAuthDetailDO(long todoDetailId, String name, String content, boolean authed) {
        this.todoDetailId = todoDetailId;
        this.name = name;
        this.systemType = 0;
        this.content = content;
        this.authed = authed;
    }

    public TodoSystemAuthDetailDO(long todoDetailId, String content, boolean authed) {
        this.todoDetailId = todoDetailId;
        this.name = "getway";
        this.systemType = 1;
        this.content = content;
        this.authed = authed;
    }


    @Override
    public String toString() {
        return "TodoSystemAuthDetailDO{" +
                "id=" + id +
                ", todoDetailId=" + todoDetailId +
                ", name='" + name + '\'' +
                ", systemType=" + systemType +
                ", content=" + content +
                ", authed=" + authed +
                ", need=" + need +
                ", processStatus=" + processStatus +
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSystemType() {
        return systemType;
    }

    public void setSystemType(int systemType) {
        this.systemType = systemType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAuthed() {
        return authed;
    }

    public void setAuthed(boolean authed) {
        this.authed = authed;
    }

    public boolean isNeed() {
        return need;
    }

    public void setNeed(boolean need) {
        this.need = need;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
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
