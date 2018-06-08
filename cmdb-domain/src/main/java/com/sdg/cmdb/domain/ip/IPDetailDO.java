package com.sdg.cmdb.domain.ip;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/11.
 */
public class IPDetailDO implements Serializable {
    private static final long serialVersionUID = -5837328039464401850L;

    public static final Integer publicIP = 0;

    public static final Integer insideIP = 1;


    private long id = -1;

    private long ipNetworkId;

    private long serverId;

    private String ip;

    /*
    0:公网;1:内网
     */
    private int ipType;


    /**
     * ip使用类型
     */
    private int ipUseType = 0;


    private String content;

    private String gmtCreate;

    private String gmtModify;

    public IPDetailDO() {
    }

    public IPDetailDO(long id) {
        this.id = id;
    }

    public IPDetailDO(long ipNetworkId, String ip, int ipType) {
        this.ipNetworkId = ipNetworkId;
        this.ip = ip;
        this.ipType = ipType;
    }

    public IPDetailDO(long ipNetworkId, String ip, Integer ipType) {
        this.ipNetworkId = ipNetworkId;
        this.ip = ip;
        this.ipType = ipType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIpNetworkId() {
        return ipNetworkId;
    }

    public void setIpNetworkId(long ipNetworkId) {
        this.ipNetworkId = ipNetworkId;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
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

    public int getIpUseType() {
        return ipUseType;
    }

    public void setIpUseType(int ipUseType) {
        this.ipUseType = ipUseType;
    }

    @Override
    public String toString() {
        return "IPDetailDO{" +
                "id=" + id +
                ", ipNetworkId=" + ipNetworkId +
                ", serverId=" + serverId +
                ", ip='" + ip + '\'' +
                ", ipType=" + ipType +
                ", content='" + content + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public enum IpUseTypeEnum {
        //0 保留
        def(0, "系统默认"),
        rm(1, "远程管理"),
        ipmi(2, "IPMI管理"),
        eth0(3, "网卡1"),
        eth1(4, "网卡2"),
        eth2(5, "网卡3"),
        eth3(6, "网卡4"),
        other(7, "其它"),
        vip(8, "虚拟"),
        backup(9, "备用");

        private int code;
        private String desc;

        IpUseTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getIpUseTypeName(int code) {
            for (IpUseTypeEnum ipUseTypeEnum : IpUseTypeEnum.values()) {
                if (ipUseTypeEnum.getCode() == code) {
                    return ipUseTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

}
