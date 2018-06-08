package com.sdg.cmdb.domain.server;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by liangjian on 2017/5/23.
 */
public class ServerUpstreamDO implements Serializable {
    private static final long serialVersionUID = -8144604658151700635L;

    private long id;

    private long serverId;

    private String insideIp;

    private String serviceAction;

    private String actionTime;

    private String gmtModify;

    private String gmtCreate;

    public ServerUpstreamDO() {

    }

    public ServerUpstreamDO(ServerDO serverDO, String serviceAction, String actionTime) {
        this.serverId = serverDO.getId();
        this.insideIp = serverDO.getInsideIp();
        this.serviceAction = serviceAction;
        this.actionTime = actionTime;
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

    public String getInsideIp() {
        return insideIp;
    }

    public void setInsideIp(String insideIp) {
        this.insideIp = insideIp;
    }

    public String getServiceAction() {
        return serviceAction;
    }

    public void setServiceAction(String serviceAction) {
        this.serviceAction = serviceAction;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
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

    @Override
    public String toString() {
        return "ServerUpstreamDO{" +
                "id=" + id +
                ", serverId=" + serverId +
                ", insideIp='" + insideIp + '\'' +
                ", serviceAction='" + serviceAction + '\'' +
                ", actionTime='" + actionTime + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

}
