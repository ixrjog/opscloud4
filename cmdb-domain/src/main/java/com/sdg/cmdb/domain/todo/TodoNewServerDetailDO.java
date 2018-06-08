package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

public class TodoNewServerDetailDO implements Serializable {

    private static final long serialVersionUID = -6869261988410982462L;

    private long id;

    private long todoDetailId;

    private int envType;

    private int instanceType;

    private int zoneType;

    private boolean allocateIp = false;

    /**
     * 处理状态 0 未处理，1 处理完成  2 ERR
     */
    private int processStatus =0;

    private int dataDiskSize = 100;

    private int serverCnt = 1;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return "TodoNewServerDetailDO{" +
                "id=" + id +
                ", todoDetailId=" + todoDetailId +
                ", envType=" + envType +
                ", instanceType=" + instanceType +
                ", zoneType=" + zoneType +
                ", allocateIp=" + allocateIp +
                ", dataDiskSize=" + dataDiskSize +
                ", serverCnt=" + serverCnt +
                ", content='" + content + '\'' +
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

    public int getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(int instanceType) {
        this.instanceType = instanceType;
    }

    public int getZoneType() {
        return zoneType;
    }

    public void setZoneType(int zoneType) {
        this.zoneType = zoneType;
    }

    public boolean isAllocateIp() {
        return allocateIp;
    }

    public void setAllocateIp(boolean allocateIp) {
        this.allocateIp = allocateIp;
    }

    public int getDataDiskSize() {
        return dataDiskSize;
    }

    public void setDataDiskSize(int dataDiskSize) {
        this.dataDiskSize = dataDiskSize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getServerCnt() {
        return serverCnt;
    }

    public void setServerCnt(int serverCnt) {
        this.serverCnt = serverCnt;
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
