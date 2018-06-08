package com.sdg.cmdb.domain.server;

import com.sdg.cmdb.domain.ip.IPDetailVO;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/23.
 */
public class ServerVO implements Serializable {
    private static final long serialVersionUID = -70862472925821073L;

    private long id;

    private ServerGroupDO serverGroupDO;

    private int loginType;

    private String loginUser;

    private int envType;

    /**
     * 环境
     */
    private String envTypeStr;

    private IPDetailVO publicIP;

    private IPDetailVO insideIP;

    private int serverType;

    private String serverName;

    private String area;

    private int useType;

    private String serialNumber;

    private String ciGroup;

    private String content;

    private int zabbixStatus;

    private int zabbixMonitor;

    private String tomcatVersion;

    private String gmtModify;

    private String gmtCreate;

    private VmServerDO vmServerDO;

    private EcsServerDO ecsServerDO;

    private PhysicalServerDO physicalServerDO;

    private ServerGroupUseTypeDO serverGroupUseTypeDO;

    public ServerVO() {
    }

    public ServerVO(ServerDO serverDO, ServerGroupDO serverGroupDO, IPDetailVO publicIP, IPDetailVO insideIP) {
        this.id = serverDO.getId();
        this.serverGroupDO = serverGroupDO;
        this.loginType = serverDO.getLoginType();
        this.loginUser = serverDO.getLoginUser();
        this.envType = serverDO.getEnvType();
        this.envTypeStr = EnvTypeEnum.getDescByCode(envType);
        this.publicIP = publicIP;
        this.insideIP = insideIP;
        this.serverType = serverDO.getServerType();
        this.serverName = serverDO.getServerName();
        this.area = serverDO.getArea();
        this.useType = serverDO.getUseType();
        this.serialNumber = serverDO.getSerialNumber();
        this.ciGroup = serverDO.getCiGroup();
        this.content = serverDO.getContent();
        this.zabbixStatus = serverDO.getZabbixStatus();
        this.zabbixMonitor = serverDO.getZabbixMonitor();
        this.tomcatVersion = serverDO.getExtTomcatVersion();
        this.gmtCreate = serverDO.getGmtCreate();
        this.gmtModify = serverDO.getGmtModify();
    }

    public ServerVO(ServerDO serverDO, ServerGroupDO serverGroupDO) {
        this.id = serverDO.getId();
        this.serverGroupDO = serverGroupDO;
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
        this.gmtModify = serverDO.getGmtModify();
    }

    public ServerVO(ServerDO serverDO) {
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
        this.gmtModify = serverDO.getGmtModify();
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

    public IPDetailVO getPublicIP() {
        return publicIP;
    }

    public void setPublicIP(IPDetailVO publicIP) {
        this.publicIP = publicIP;
    }

    public IPDetailVO getInsideIP() {
        return insideIP;
    }

    public void setInsideIP(IPDetailVO insideIP) {
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

    public VmServerDO getVmServerDO() {
        return vmServerDO;
    }

    public void setVmServerDO(VmServerDO vmServerDO) {
        this.vmServerDO = vmServerDO;
    }

    public EcsServerDO getEcsServerDO() {
        return ecsServerDO;
    }

    public void setEcsServerDO(EcsServerDO ecsServerDO) {
        this.ecsServerDO = ecsServerDO;
    }

    public PhysicalServerDO getPhysicalServerDO() {
        return physicalServerDO;
    }

    public void setPhysicalServerDO(PhysicalServerDO physicalServerDO) {
        this.physicalServerDO = physicalServerDO;
    }

    public String getTomcatVersion() {
        return tomcatVersion;
    }

    public void setTomcatVersion(String tomcatVersion) {
        this.tomcatVersion = tomcatVersion;
    }

    public ServerGroupUseTypeDO getServerGroupUseTypeDO() {
        return serverGroupUseTypeDO;
    }

    public void setServerGroupUseTypeDO(ServerGroupUseTypeDO serverGroupUseTypeDO) {
        this.serverGroupUseTypeDO = serverGroupUseTypeDO;
    }

    public String acqServerName() {
        if (this.envType == ServerDO.EnvTypeEnum.prod.getCode()) {
            return serverName;
        } else {
            return serverName + "-" + ServerDO.EnvTypeEnum.getEnvTypeName(envType);
        }
    }

    @Override
    public String toString() {
        return "ServerVO{" +
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
}
