package com.sdg.cmdb.domain.server;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/5.
 */
public class VmTemplateDO implements Serializable {
    private static final long serialVersionUID = -2329822114440981619L;

    private long id;

    private long serverId;

    private String name;

    private int cpu;

    private int memory;

    private String insideIp;

    private int sysDiskSize;

    private int dataDiskSize;

    private String gmtModify;

    private String gmtCreate;

    @Override
    public String toString() {
        return "VmTemplateDO{" +
                "id=" + id +
                ", serverId='" + serverId + '\'' +
                ", name='" + name + '\'' +
                ", cpu=" + cpu +
                ", memory='" + memory +
                ", insideIp='" + insideIp + '\'' +
                ", sysDiskSize='" + sysDiskSize + '\'' +
                ", dataDiskSize='" + dataDiskSize + '\'' +
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

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public String getInsideIp() {
        return insideIp;
    }

    public void setInsideIp(String insideIp) {
        this.insideIp = insideIp;
    }

    public int getSysDiskSize() {
        return sysDiskSize;
    }

    public void setSysDiskSize(int sysDiskSize) {
        this.sysDiskSize = sysDiskSize;
    }

    public int getDataDiskSize() {
        return dataDiskSize;
    }

    public void setDataDiskSize(int dataDiskSize) {
        this.dataDiskSize = dataDiskSize;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
