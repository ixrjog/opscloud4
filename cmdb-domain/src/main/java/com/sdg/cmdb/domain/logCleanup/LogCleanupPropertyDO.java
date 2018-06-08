package com.sdg.cmdb.domain.logCleanup;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/3/30.
 */
public class LogCleanupPropertyDO implements Serializable {
    private static final long serialVersionUID = 5950320978859804002L;

    private long id;

    private long serverGroupId;

    private String groupName;

    private long serverId;

    private String serverName;

    private String serialNumber;

    //磁盘使用率
    private int diskRate;

    //归档时间（天）
    private float history;

    private int envType;

    //是否启用
    private boolean enabled;
    //清理结果
    private boolean cleanupResult;

    //清理时间
    private String cleanupTime;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(long serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getDiskRate() {
        return diskRate;
    }

    public void setDiskRate(int diskRate) {
        this.diskRate = diskRate;
    }

    public float getHistory() {
        return history;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isCleanupResult() {
        return cleanupResult;
    }

    public void setCleanupResult(boolean cleanupResult) {
        this.cleanupResult = cleanupResult;
    }

    public void setHistory(float history) {
        this.history = history;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }

    public String getCleanupTime() {
        return cleanupTime;
    }


    public void setCleanupTime(String cleanupTime) {
        this.cleanupTime = cleanupTime;
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


    public String acqServerName() {
        if (this.envType == ServerDO.EnvTypeEnum.prod.getCode()) {
            return serverName + "-" + serialNumber;
        } else {
            return serverName + "-" + ServerDO.EnvTypeEnum.getEnvTypeName(envType) + "-" + serialNumber;
        }
    }

    public LogCleanupPropertyDO() {
    }

    public LogCleanupPropertyDO(ServerGroupDO serverGroupDO, ServerDO serverDO) {
        this.serverGroupId = serverGroupDO.getId();
        this.groupName = serverGroupDO.getName();
        this.serverId = serverDO.getId();
        this.serverName = serverDO.getServerName();
        this.serialNumber = serverDO.getSerialNumber();
        this.envType = serverDO.getEnvType();
        this.enabled = true;
    }

    @Override
    public String toString() {
        return "LogCleanupPropertyDO{" +
                "id=" + id +
                ", serverGroupId=" + serverGroupId +
                ", groupName='" + groupName + '\'' +
                ", serverId=" + serverId +
                ", serverName=" + serverName +
                ", serialNumber=" + serialNumber +
                ", diskRate=" + diskRate +
                ", history=" + history +
                ", envType=" + envType +
                ", enabled=" + enabled +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

}
