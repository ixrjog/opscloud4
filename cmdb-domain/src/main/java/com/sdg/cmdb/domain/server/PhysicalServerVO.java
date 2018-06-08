package com.sdg.cmdb.domain.server;

import com.sdg.cmdb.domain.esxi.EsxiHostDO;
import com.sdg.cmdb.domain.ip.IPDetailDO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liangjian on 2017/2/13.
 */
public class PhysicalServerVO implements Serializable {

    private static final long serialVersionUID = -1276208900811713764L;

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

    private EsxiHostDO esxiHostDO;

    private ServerPerfVO serverPerfVO;

    private List<IPDetailDO> ipDetailList;

    public List<IPDetailDO> getIpDetailList() {
        return ipDetailList;
    }

    public void setIpDetailList(List<IPDetailDO> ipDetailList) {
        this.ipDetailList = ipDetailList;
    }

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

    public EsxiHostDO getEsxiHostDO() {
        return esxiHostDO;
    }

    public void setEsxiHostDO(EsxiHostDO esxiHostDO) {
        this.esxiHostDO = esxiHostDO;
    }

    public ServerPerfVO getServerPerfVO() {
        return serverPerfVO;
    }

    public void setServerPerfVO(ServerPerfVO serverPerfVO) {
        this.serverPerfVO = serverPerfVO;
    }

    public PhysicalServerVO() {
    }

    public PhysicalServerVO(PhysicalServerDO physicalServerDO) {
        this.id = physicalServerDO.getId();
        this.serverId = physicalServerDO.getServerId();
        this.serverName = physicalServerDO.getServerName();
        this.useType = physicalServerDO.getUseType();
        this.content = physicalServerDO.getContent();
        this.cpu = physicalServerDO.getCpu();
        this.memory = physicalServerDO.getMemory();
        this.gmtCreate = physicalServerDO.getGmtCreate();
        this.gmtModify = physicalServerDO.getGmtModify();
        this.systemAssetTag = physicalServerDO.getSystemAssetTag();
    }

    @Override
    public String toString() {
        return "PhysicalServerVO{" +
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
