package com.sdg.cmdb.domain.server;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/2/13.
 */
public class PhysicalServerDO implements Serializable {

    private static final long serialVersionUID = 7469555992944110469L;

    private long id;

    private String content;

    private String serverName;

    private int useType;

    private long serverId;

    private String gmtModify;

    private String gmtCreate;

    private String insideIp;

    private String publicIp;

    private String remoteManagementIp;

    private int cpu;

    private int cpuCnt;

    private String cpuModel;

    private int memory;

    //系统资产标签
    private String systemAssetTag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
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

    public String getRemoteManagementIp() {
        return remoteManagementIp;
    }

    public void setRemoteManagementIp(String remoteManagementIp) {
        this.remoteManagementIp = remoteManagementIp;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getCpuCnt() {
        return cpuCnt;
    }

    public void setCpuCnt(int cpuCnt) {
        this.cpuCnt = cpuCnt;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public String getSystemAssetTag() {
        return systemAssetTag;
    }

    public void setSystemAssetTag(String systemAssetTag) {
        this.systemAssetTag = systemAssetTag;
    }

    public enum UseTypeEnum {
        //0 保留
        physical(0, "物理机"),
        vm(1, "虚拟化"),
        bi(2, "数据挖掘"),
        other(9, "其它用途");
        private int code;
        private String desc;

        UseTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getUseTypeName(int code) {
            for (UseTypeEnum useTypeEnum : UseTypeEnum.values()) {
                if (useTypeEnum.getCode() == code) {
                    return useTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    @Override
    public String toString() {
        return "PhysicalServerDO{" +
                "id=" + id +
                "content='" + content + '\'' +
                ", serverName='" + serverName + '\'' +
                ", serverId='" + serverId + '\'' +
                ", insideIp='" + insideIp + '\'' +
                ", publicIp='" + publicIp + '\'' +
                ", cpu=" + cpu +
                ", memory=" + memory +
                ", useType=" + useType +
                ", remoteManagementIp='" + remoteManagementIp + '\'' +
                ", cpuModel='" + cpuModel + '\'' +
                ", cpuCnt='" + cpuCnt + '\'' +
                ", systemAssetTag='" + systemAssetTag + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }



}
