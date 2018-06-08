package com.sdg.cmdb.domain.aliyun;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/13.
 */
public class AliyunNetworkDO implements Serializable {
    private static final long serialVersionUID = -3193402870086464699L;

    public static final int NETWORK_VPC = 1;

    private long id;

    private String networkType;

    private String networkDesc;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getNetworkDesc() {
        return networkDesc;
    }

    public void setNetworkDesc(String networkDesc) {
        this.networkDesc = networkDesc;
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
        return "AliyunEcsImageDO{" +
                "id=" + id +
                ", networkType='" + networkType + '\'' +
                ", networkDesc='" + networkDesc + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

    public enum NetworkTypeEnum {
        //0 保留
        vpc(0, "vpc"),
        classic(1, "classic");
        private int code;
        private String desc;

        NetworkTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getNetworkTypeName(int code) {
            for (NetworkTypeEnum networkTypeEnum : NetworkTypeEnum.values()) {
                if (networkTypeEnum.getCode() == code) {
                    return networkTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    /**
     * 网络计费类型
     */
    public enum InternetChargeTypeEnum {
        // 按带宽计费
        bandwidth(0, "PayByBandwidth"),
        // 按流量计费(默认值)
        traffic(1, "PayByTraffic");
        private int code;
        private String desc;

        InternetChargeTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getInternetChargeTypeName(int code) {
            for (InternetChargeTypeEnum internetChargeTypeEnum : InternetChargeTypeEnum.values()) {
                if (internetChargeTypeEnum.getCode() == code) {
                    return internetChargeTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }



}
