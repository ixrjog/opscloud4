package com.sdg.cmdb.domain.logCleanup;

import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/12.
 */
public class LogCleanupPropertyVO implements Serializable {


    private static final long serialVersionUID = 4174200334195133594L;

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

    private boolean cleanupResult;

    //清理时间
    private String cleanupTime;

    private String insideIp;

    private String publicIp;

    private int serverType;


    public LogCleanupPropertyVO(LogCleanupPropertyDO logCleanupPropertyDO, ServerDO serverDO) {
        this.groupName = logCleanupPropertyDO.getGroupName();
        this.serverId = logCleanupPropertyDO.getServerId();
        this.serverName = logCleanupPropertyDO.getServerName();
        this.serialNumber = logCleanupPropertyDO.getSerialNumber();
        this.diskRate = logCleanupPropertyDO.getDiskRate();
        this.history = logCleanupPropertyDO.getHistory();
        this.envType = logCleanupPropertyDO.getEnvType();
        this.enabled = logCleanupPropertyDO.isEnabled();
        this.cleanupResult = logCleanupPropertyDO.isCleanupResult();
        this.cleanupTime = logCleanupPropertyDO.getCleanupTime();
        if (serverDO == null) return;
        this.insideIp = serverDO.getInsideIp();
        this.publicIp = serverDO.getPublicIp();
        this.serverType = serverDO.getServerType();
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

    public void setHistory(float history) {
        this.history = history;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
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

    public String getCleanupTime() {
        return cleanupTime;
    }

    public void setCleanupTime(String cleanupTime) {
        this.cleanupTime = cleanupTime;
    }

    public String getInsideIp() {
        return insideIp;
    }

    public void setInsideIp(String insideIp) {
        this.insideIp = insideIp;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }
}
