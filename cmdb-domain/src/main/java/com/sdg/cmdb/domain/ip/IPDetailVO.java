package com.sdg.cmdb.domain.ip;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/11.
 */
public class IPDetailVO implements Serializable {
    private static final long serialVersionUID = -7448639708576992865L;

    private long id;

    private IPNetworkDO ipNetworkDO;

    private long serverId;

    private String serverName;

    private String ip;

    private int ipType;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    public IPDetailVO() {
    }

    public IPDetailVO(IPDetailDO detailDO, IPNetworkDO ipNetworkDO, String serverName) {
        this.id = detailDO.getId();
        this.ipNetworkDO = ipNetworkDO;
        this.serverId = detailDO.getServerId();
        this.serverName = serverName;
        this.ip = detailDO.getIp();
        this.ipType = detailDO.getIpType();
        this.content = detailDO.getContent();
        this.gmtCreate = detailDO.getGmtCreate();
        this.gmtModify = detailDO.getGmtModify();
    }

    public IPDetailVO(String ip, IPNetworkDO ipNetworkDO) {
        this.ip = ip;
        this.ipNetworkDO = ipNetworkDO;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public IPNetworkDO getIpNetworkDO() {
        return ipNetworkDO;
    }

    public void setIpNetworkDO(IPNetworkDO ipNetworkDO) {
        this.ipNetworkDO = ipNetworkDO;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getIpType() {
        return ipType;
    }

    public void setIpType(int ipType) {
        this.ipType = ipType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return "IPDetailVO{" +
                "id=" + id +
                ", ipNetworkDO=" + ipNetworkDO +
                ", serverId=" + serverId +
                ", serverName='" + serverName + '\'' +
                ", ip='" + ip + '\'' +
                ", ipType=" + ipType +
                ", content='" + content + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
