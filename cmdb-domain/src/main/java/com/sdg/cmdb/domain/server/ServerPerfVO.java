package com.sdg.cmdb.domain.server;

import com.sdg.cmdb.domain.ip.IPDetailVO;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/2/25.
 */
public class ServerPerfVO implements Serializable {

    private static final long serialVersionUID = 3726464752161593415L;

    private long id;

    private ServerGroupDO serverGroupDO;

    private int loginType;

    private String loginUser;

    private int envType;

    /**
     * 环境
     */
    private String envTypeStr;

    private String publicIP;

    private String insideIP;

    private int serverType;

    private String serverName;

    private String area;

    private int useType;

    private String serialNumber;

    private String ciGroup;

    private String content;

    private int zabbixStatus;

    private int zabbixMonitor;

    private String cpuUser = "-1";

    private String load1 = "-1";

    private String load5 = "-1";

    private String load15 = "-1";

    private String memoryRate = "-1";

    private String diskRate = "-1";

    private String diskSysRate = "-1";

    private float diskSysRateValue = 0f;

    private float diskDataRateValue = 0f;

    private String diskDataRate = "-1";

    private String diskSysBps = "-1";

    private String diskSysIops = "-1";

    private String diskSysReadMs = "-1";

    private String diskSysWriteMs = "-1";

    private String diskDataBps = "-1";

    private String diskDataIops = "-1";

    private String diskDataReadMs = "-1";

    private String diskDataWriteMs = "-1";

    private String cpuIowait = "-1";

    private String gmtModify;

    private String gmtCreate;

    public ServerPerfVO() {
    }

    public ServerPerfVO(ServerDO serverDO) {
        this.id = serverDO.getId();
        this.loginType = serverDO.getLoginType();
        this.loginUser = serverDO.getLoginUser();
        this.envType = serverDO.getEnvType();
        this.envTypeStr = EnvTypeEnum.getDescByCode(envType);
        this.serverType = serverDO.getServerType();
        this.serverName = serverDO.getServerName();
        this.area = serverDO.getArea();
        this.useType = serverDO.getUseType();
        this.serialNumber = serverDO.getSerialNumber();
        this.ciGroup = serverDO.getCiGroup();
        this.content = serverDO.getContent();
        this.zabbixStatus = serverDO.getZabbixStatus();
        this.zabbixMonitor = serverDO.getZabbixMonitor();
        this.gmtCreate = serverDO.getGmtCreate();
    }

    public ServerPerfVO(ServerDO serverDO, ServerGroupDO serverGroupDO) {
        this.id = serverDO.getId();
        this.serverGroupDO = serverGroupDO;
        this.loginType = serverDO.getLoginType();
        this.loginUser = serverDO.getLoginUser();
        this.envType = serverDO.getEnvType();
        this.envTypeStr = EnvTypeEnum.getDescByCode(envType);
        this.serverType = serverDO.getServerType();
        this.serverName = serverDO.getServerName();
        this.publicIP = serverDO.getPublicIp();
        this.insideIP = serverDO.getInsideIp();
        this.area = serverDO.getArea();
        this.useType = serverDO.getUseType();
        this.serialNumber = serverDO.getSerialNumber();
        this.ciGroup = serverDO.getCiGroup();
        this.content = serverDO.getContent();
        this.zabbixStatus = serverDO.getZabbixStatus();
        this.zabbixMonitor = serverDO.getZabbixMonitor();
        this.gmtCreate = serverDO.getGmtCreate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }

    public String getEnvTypeStr() {
        return envTypeStr;
    }

    public void setEnvTypeStr(String envTypeStr) {
        this.envTypeStr = envTypeStr;
    }

    public String getCpuIowait() {
        return cpuIowait;
    }

    public void setCpuIowait(String cpuIowait) {
        this.cpuIowait = cpuIowait;
    }

    public String getPublicIP() {
        return publicIP;
    }

    public void setPublicIP(String publicIP) {
        this.publicIP = publicIP;
    }

    public String getInsideIP() {
        return insideIP;
    }

    public void setInsideIP(String insideIP) {
        this.insideIP = insideIP;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCiGroup() {
        return ciGroup;
    }

    public void setCiGroup(String ciGroup) {
        this.ciGroup = ciGroup;
    }

    public String getContent() {
        return content;
    }

    public int getZabbixStatus() {
        return zabbixStatus;
    }

    public void setZabbixStatus(int zabbixStatus) {
        this.zabbixStatus = zabbixStatus;
    }

    public int getZabbixMonitor() {
        return zabbixMonitor;
    }

    public void setZabbixMonitor(int zabbixMonitor) {
        this.zabbixMonitor = zabbixMonitor;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getCpuUser() {
        return cpuUser;
    }

    public void setCpuUser(String cpuUser) {
        this.cpuUser = cpuUser;
    }

    public String getLoad1() {
        return load1;
    }

    public void setLoad1(String load1) {
        this.load1 = load1;
    }

    public String getLoad5() {
        return load5;
    }

    public void setLoad5(String load5) {
        this.load5 = load5;
    }

    public String getLoad15() {
        return load15;
    }

    public void setLoad15(String load15) {
        this.load15 = load15;
    }

    public String getMemoryRate() {
        return memoryRate;
    }

    public void setMemoryRate(String memoryRate) {
        this.memoryRate = memoryRate;
    }

    public String getDiskRate() {
        return diskRate;
    }

    public String getDiskSysRate() {
        return diskSysRate;
    }

    public void setDiskSysRate(String diskSysRate) {
        this.diskSysRate = diskSysRate;
    }

    public String getDiskDataRate() {
        return diskDataRate;
    }

    public void setDiskDataRate(String diskDataRate) {
        this.diskDataRate = diskDataRate;
    }

    public void setDiskRate(String diskRate) {
        this.diskRate = diskRate;
    }

    public String getDiskSysIops() {
        return diskSysIops;
    }

    public void setDiskSysIops(String diskSysIops) {
        this.diskSysIops = diskSysIops;
    }

    public String getDiskSysBps() {
        return diskSysBps;
    }

    public void setDiskSysBps(String diskSysBps) {
        this.diskSysBps = diskSysBps;
    }

    public String getDiskDataBps() {
        return diskDataBps;
    }

    public void setDiskDataBps(String diskDataBps) {
        this.diskDataBps = diskDataBps;
    }

    public String getDiskDataIops() {
        return diskDataIops;
    }

    public void setDiskDataIops(String diskDataIops) {
        this.diskDataIops = diskDataIops;
    }

    public String getDiskSysReadMs() {
        return diskSysReadMs;
    }

    public void setDiskSysReadMs(String diskSysReadMs) {
        this.diskSysReadMs = diskSysReadMs;
    }

    public String getDiskSysWriteMs() {
        return diskSysWriteMs;
    }

    public void setDiskSysWriteMs(String diskSysWriteMs) {
        this.diskSysWriteMs = diskSysWriteMs;
    }

    public String getDiskDataReadMs() {
        return diskDataReadMs;
    }

    public void setDiskDataReadMs(String diskDataReadMs) {
        this.diskDataReadMs = diskDataReadMs;
    }

    public String getDiskDataWriteMs() {
        return diskDataWriteMs;
    }

    public void setDiskDataWriteMs(String diskDataWriteMs) {
        this.diskDataWriteMs = diskDataWriteMs;
    }

    @Override
    public String toString() {
        return "ServerPerfVO{" +
                "id=" + id +
                ", serverGroupDO=" + serverGroupDO +
                ", loginType=" + loginType +
                ", loginUser='" + loginUser + '\'' +
                ", envType=" + envType +
                ", envTypeStr='" + envTypeStr + '\'' +
                ", publicIP=" + publicIP +
                ", insideIP=" + insideIP +
                ", serverType=" + serverType +
                ", serverName='" + serverName + '\'' +
                ", area='" + area + '\'' +
                ", useType=" + useType +
                ", serialNumber='" + serialNumber + '\'' +
                ", ciGroup='" + ciGroup + '\'' +
                ", content='" + content + '\'' +
                ", zabbixStatus=" + zabbixStatus +
                ", zabbixMonitor=" + zabbixMonitor +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

    public String perfInfo() {
        return serverName + "-" + serialNumber + ":" + ServerDO.EnvTypeEnum.getEnvTypeName(envType) +
                "{cpuUser=" + cpuUser +
                ",load1=" + load1 +
                ",load5=" + load5 +
                ",load15=" + load15 +
                ",memoryRate=" + memoryRate +
                ",diskRate=" + diskRate +
                ",diskSysBps=" + diskSysBps +
                ",diskSysIops=" + diskSysIops +
                ",diskDataBps=" + diskDataBps +
                ",diskDataIops=" + diskDataIops +
                "}";
    }

}
