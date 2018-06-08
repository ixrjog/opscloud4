package com.sdg.cmdb.domain.server;

/**
 * Created by liangjian on 2017/2/9.
 */

import java.io.Serializable;

/**
 * 服务器统计
 */
public class ServerStatisticsDO implements Serializable {

    private static final long serialVersionUID = 3211192279105640642L;
    /**
     * 服务器总数
     */
    private int cnt;

    private int memoryCnt;

    private int memoryRate;

    private int cpuCnt;

    private int cpuUser;

    private float load;

    private int bandwidthCnt;

    private int serverType;


    /**
     * 利用率 整数 100 = 100%
     */
    private int rate;

    /**
     * 总成本
     */
    private int cost;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCpuCnt() {
        return cpuCnt;
    }

    public void setCpuCnt(int cpuCnt) {
        this.cpuCnt = cpuCnt;
    }

    public int getMemoryCnt() {
        return memoryCnt;
    }

    public void setMemoryCnt(int memoryCnt) {
        this.memoryCnt = memoryCnt;
    }

    public int getBandwidthCnt() {
        return bandwidthCnt;
    }

    public void setBandwidthCnt(int bandwidthCnt) {
        this.bandwidthCnt = bandwidthCnt;
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

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getMemoryRate() {
        return memoryRate;
    }

    public void setMemoryRate(int memoryRate) {
        this.memoryRate = memoryRate;
    }

    public int getCpuUser() {
        return cpuUser;
    }

    public void setCpuUser(int cpuUser) {
        this.cpuUser = cpuUser;
    }

    public float getLoad() {
        return load;
    }

    public void setLoad(float load) {
        this.load = load;
    }

    public enum ServerTypeEnum {
        //0 保留
        physicalServer(0, "物理机"),
        vmServer(1, "虚拟机"),
        ecsServer(2, "阿里云");
        private int code;
        private String desc;

        ServerTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getServerTypeName(int code) {
            for (ServerTypeEnum serverTypeEnum : ServerTypeEnum.values()) {
                if (serverTypeEnum.getCode() == code) {
                    return serverTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    @Override
    public String toString() {
        return "ServerStatisticsDO{" +
                "cnt=" + cnt +
                ", memoryCnt=" + memoryCnt +
                ", cpuCnt=" + cpuCnt +
                ", bandwidthCnt=" + bandwidthCnt  +
                ", serverType=" + serverType +
                ", serverTypeName='" + ServerStatisticsDO.ServerTypeEnum.getServerTypeName(serverType) + '\'' +
                '}';
    }





}
