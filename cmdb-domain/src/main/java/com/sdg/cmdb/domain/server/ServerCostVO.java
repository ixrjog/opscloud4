package com.sdg.cmdb.domain.server;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/2/23.
 */
public class ServerCostVO implements Serializable {

    private static final long serialVersionUID = 5597844457194658921L;

    private String serverName;

    private String content;

    private int cpu;

    // 单位MB
    private int memory;

    private String gmtCreate;

    private int serverType;

    private int cost;

    private long serverId;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public ServerCostVO(VmServerDO vmServerDO) {
        this.serverName = vmServerDO.getServerName();
        this.content = vmServerDO.getContent();
        this.cpu = vmServerDO.getCpu();
        this.memory = vmServerDO.getMemory();
        this.serverType = ServerDO.ServerTypeEnum.vm.getCode();
        this.gmtCreate = vmServerDO.getGmtCreate();
        this.serverId=vmServerDO.getServerId();
    }

    public ServerCostVO(EcsServerDO ecsServerDO) {
        this.serverName = ecsServerDO.getServerName();
        this.content = ecsServerDO.getContent();
        this.cpu = ecsServerDO.getCpu();
        this.memory = ecsServerDO.getMemory();
        this.serverType = ServerDO.ServerTypeEnum.ecs.getCode();
        this.gmtCreate = ecsServerDO.getGmtCreate();
        this.serverId=ecsServerDO.getServerId();
    }

    @Override
    public String toString() {
        return "ServerCostVO{" +
                ", serverType=" + serverType +
                ", serverName='" + serverName + '\'' +
                ", cpu=" + cpu +
                ", memory=" + memory +
                ", content='" + content + '\'' +
                ", cost='" + cost +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

}
