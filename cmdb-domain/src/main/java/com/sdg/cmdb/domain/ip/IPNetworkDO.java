package com.sdg.cmdb.domain.ip;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/9.
 */
public class IPNetworkDO implements Serializable {
    private static final long serialVersionUID = 6190300582761894742L;

    private long id;

    private long serverGroupId;

    private String ipNetwork;

    private String gateWay;

    private String dnsOne;

    private String dnsTwo;

    private int ipType;

    private String content;

    private String produceMark;

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

    public String getIpNetwork() {
        return ipNetwork;
    }

    public void setIpNetwork(String ipNetwork) {
        this.ipNetwork = ipNetwork;
    }

    public String getGateWay() {
        return gateWay;
    }

    public void setGateWay(String gateWay) {
        this.gateWay = gateWay;
    }

    public String getDnsOne() {
        return dnsOne;
    }

    public void setDnsOne(String dnsOne) {
        this.dnsOne = dnsOne;
    }

    public String getDnsTwo() {
        return dnsTwo;
    }

    public void setDnsTwo(String dnsTwo) {
        this.dnsTwo = dnsTwo;
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

    public String getProduceMark() {
        return produceMark;
    }

    public void setProduceMark(String produceMark) {
        this.produceMark = produceMark;
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
        return "IPNetworkDO{" +
                "id=" + id +
                ", serverGroupId=" + serverGroupId +
                ", ipNetwork='" + ipNetwork + '\'' +
                ", gateWay='" + gateWay + '\'' +
                ", dnsOne='" + dnsOne + '\'' +
                ", dnsTwo='" + dnsTwo + '\'' +
                ", ipType=" + ipType +
                ", content='" + content + '\'' +
                ", produceMark='" + produceMark + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
