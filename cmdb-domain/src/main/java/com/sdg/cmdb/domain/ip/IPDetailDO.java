package com.sdg.cmdb.domain.ip;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/11.
 */
@Data
public class IPDetailDO implements Serializable {
    private static final long serialVersionUID = -5837328039464401850L;

    public static final int PUBLIC_IP = 0;

    public static final int INSIDE_IP = 1;


    private long id = -1;

    private long ipNetworkId;

    private long serverId;

    private String ip;

    /**
     * 0:公网;1:内网
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

    public IPDetailDO(long serverId, long ipNetworkId, String ip, int ipType) {
        this.serverId = serverId;
        this.ipNetworkId = ipNetworkId;
        this.ip = ip;
        this.ipType = ipType;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
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
