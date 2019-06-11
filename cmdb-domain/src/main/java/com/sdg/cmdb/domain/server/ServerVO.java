package com.sdg.cmdb.domain.server;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.ip.IPDetailVO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/23.
 */
@Data
public class ServerVO implements Serializable {
    private static final long serialVersionUID = -70862472925821073L;

    private long id;
    private ServerGroupDO serverGroupDO;
    private int loginType;
    private String loginUser;
    private int envType;
    private EnvType env;
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
        this.env = new EnvType(this.envType);
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
        this.env = new EnvType(this.envType);
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
        this.env = new EnvType(this.envType);
        this.gmtCreate = serverDO.getGmtCreate();
        this.gmtModify = serverDO.getGmtModify();
    }

    public String acqServerName() {
        if (this.envType == EnvType.EnvTypeEnum.prod.getCode()) {
            return serverName + "-" + serialNumber;
        } else {
            return serverName + "-" + EnvType.EnvTypeEnum.getEnvTypeName(envType) + "-" + serialNumber;
        }
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
