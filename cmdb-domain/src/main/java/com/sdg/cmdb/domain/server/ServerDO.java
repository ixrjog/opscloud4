package com.sdg.cmdb.domain.server;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/6.
 */
public class ServerDO implements Serializable {
    private static final long serialVersionUID = -8455519494554601001L;
    //物理服务器
    //public static final int serverTypePs = 0;
    //虚拟机
    //public static final int serverTypeVm = 1;
    // aliyun ECS
    //public static final int serverTypeEcs = 2;

    private long id;

    private long serverGroupId;

    private int loginType;

    private String loginUser;

    private int envType;

    private String publicIp;

    private long publicIpId;

    private String insideIp;

    private long insideIpId;

    private int serverType;

    private String serverName;

    private String area;

    private int useType;

    private String serialNumber;

    private String ciGroup;

    private String content;

    private int zabbixStatus;

    private int zabbixMonitor;

    // 扩展字段 tomcatVersion
    private String extTomcatVersion;

    private String gmtModify;

    private String gmtCreate;

    public ServerDO() {
    }

    public ServerDO(ServerVO serverVO) {
        this.id = serverVO.getId();
        this.serverGroupId = serverVO.getServerGroupDO() == null ? 0l : serverVO.getServerGroupDO().getId();
        this.loginType = serverVO.getLoginType();
        this.loginUser = serverVO.getLoginUser();
        this.envType = serverVO.getEnvType();
        this.publicIp = serverVO.getPublicIP() == null ? "" : serverVO.getPublicIP().getIp();
        this.publicIpId = serverVO.getPublicIP() == null ? 0l : serverVO.getPublicIP().getId();
        this.insideIp = serverVO.getInsideIP() == null ? "" : serverVO.getInsideIP().getIp();
        this.insideIpId = serverVO.getInsideIP() == null ? 0l : serverVO.getInsideIP().getId();
        this.serverType = serverVO.getServerType();
        this.serverName = serverVO.getServerName();
        this.area = serverVO.getArea();
        this.useType = serverVO.getUseType();
        this.serialNumber = serverVO.getSerialNumber();
        this.ciGroup = serverVO.getCiGroup();
        this.content = serverVO.getContent();
        this.gmtCreate = serverVO.getGmtCreate();
        this.gmtModify = serverVO.getGmtModify();
    }

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

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public long getPublicIpId() {
        return publicIpId;
    }

    public void setPublicIpId(long publicIpId) {
        this.publicIpId = publicIpId;
    }

    public String getInsideIp() {
        return insideIp;
    }

    public void setInsideIp(String insideIp) {
        this.insideIp = insideIp;
    }

    public long getInsideIpId() {
        return insideIpId;
    }

    public void setInsideIpId(long insideIpId) {
        this.insideIpId = insideIpId;
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

    public void setContent(String content) {
        this.content = content;
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

    public String getExtTomcatVersion() {
        return extTomcatVersion;
    }

    public void setExtTomcatVersion(String extTomcatVersion) {
        this.extTomcatVersion = extTomcatVersion;
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

    /**
     * 带列号
     * @return
     */
    public String acqServerName() {
        if (this.envType == ServerDO.EnvTypeEnum.prod.getCode()) {
            return serverName + "-" + serialNumber;
        } else {
            return serverName + "-" + ServerDO.EnvTypeEnum.getEnvTypeName(envType) + "-" + serialNumber;
        }
    }

    /**
     * 不带列号
     * @return
     */
    public String acqHostname() {
        if (this.envType == ServerDO.EnvTypeEnum.prod.getCode()) {
            return serverName ;
        } else {
            return serverName + "-" + ServerDO.EnvTypeEnum.getEnvTypeName(envType);
        }
    }

    @Override
    public String toString() {
        return "ServerDO{" +
                "id=" + id +
                ", serverGroupId=" + serverGroupId +
                ", loginType=" + loginType +
                ", loginUser='" + loginUser + '\'' +
                ", envType=" + envType +
                ", publicIp='" + publicIp + '\'' +
                ", publicIpId=" + publicIpId +
                ", insideIp='" + insideIp + '\'' +
                ", insideIpId=" + insideIpId +
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

    public enum EnvTypeEnum {
        //0 保留／在组中代表的是所有权限
        keep(0, "default"),
        dev(1, "dev"),
        daily(2, "daily"),
        gray(3, "gray"),
        prod(4, "prod"),
        test(5, "test"),
        back(6, "back");
        private int code;
        private String desc;

        EnvTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getEnvTypeName(int code) {
            for (EnvTypeEnum envTypeEnum : EnvTypeEnum.values()) {
                if (envTypeEnum.getCode() == code) {
                    return envTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    public enum ServerTypeEnum {
        //0 保留／在组中代表的是所有权限
        ps(0, "PS"),
        vm(1, "VM"),
        ecs(2, "ECS");
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
}
