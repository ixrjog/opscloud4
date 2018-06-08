package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/9/11.
 */
public class TodoCiUserGroupDetailDO implements Serializable {
    private static final long serialVersionUID = -6310897298970432441L;

    private long id;

    private long todoDetailId;


    private long ciUserGroupId;

    private String ciUserGroupName;

    private long serverGroupId;

    private String serverGroupName;

    private int envType;

    /**
     * 处理状态 0 未处理，1 处理完成  2 ERR
     */
    private int processStatus =0;

    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return "TodoCiUserGroupDetailDO{" +
                "id=" + id +
                ", todoDetailId=" + todoDetailId +
                ", ciUserGroupId=" + ciUserGroupId +
                ", ciUserGroupName='" + ciUserGroupName + '\'' +
                ", serverGroupId=" + serverGroupId +
                ", serverGroupName='" + serverGroupName + '\'' +
                ", envType=" + envType +
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

    public long getCiUserGroupId() {
        return ciUserGroupId;
    }

    public void setCiUserGroupId(long ciUserGroupId) {
        this.ciUserGroupId = ciUserGroupId;
    }

    public String getCiUserGroupName() {
        return ciUserGroupName;
    }

    public void setCiUserGroupName(String ciUserGroupName) {
        this.ciUserGroupName = ciUserGroupName;
    }

    public long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(long serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public String getServerGroupName() {
        return serverGroupName;
    }

    public void setServerGroupName(String serverGroupName) {
        this.serverGroupName = serverGroupName;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
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
