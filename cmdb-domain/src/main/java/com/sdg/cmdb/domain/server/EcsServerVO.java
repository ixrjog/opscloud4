package com.sdg.cmdb.domain.server;



import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/6.
 */
@EqualsAndHashCode(callSuper=false)
@Data
public class EcsServerVO extends EcsServerDO implements Serializable {
    private static final long serialVersionUID = 5610868520720405528L;

    public EcsServerVO() {

    }

    public EcsServerVO(EcsServerDO ecsServerDO, ServerDO serverDO) {
        this.envType = serverDO.getEnvType();
        this.serverType = serverDO.getServerType();
        this.useType = serverDO.getUseType();
        this.serialNumber = serverDO.getSerialNumber();
        setServerName(serverDO.getServerName());
        setInstanceId(ecsServerDO.getInstanceId());
        setInsideIp(ecsServerDO.getInsideIp());
        setPublicIp(ecsServerDO.getPublicIp());
    }

    public EcsServerVO(EcsServerDO ecsServerDO) {
        setInstanceId(ecsServerDO.getInstanceId());
        setServerId(ecsServerDO.getServerId());
        setServerName(ecsServerDO.getServerName());
        setPublicIp(ecsServerDO.getPublicIp());
        setInsideIp(ecsServerDO.getInsideIp());
        // internetMaxBandwidthOut
        setInternetMaxBandwidthOut(ecsServerDO.getInternetMaxBandwidthOut());
        setCpu(ecsServerDO.getCpu());
        setMemory(ecsServerDO.getMemory());

        setSystemDiskCategory(ecsServerDO.getSystemDiskCategory());
        setSystemDiskSize(ecsServerDO.getSystemDiskSize());

        setDataDiskCategory(ecsServerDO.getDataDiskCategory());
        setDataDiskSize(ecsServerDO.getDataDiskSize());
        setArea(ecsServerDO.getArea());
        setFinance(ecsServerDO.isFinance());
        setStatus(ecsServerDO.getStatus());
        setExpiredTime(ecsServerDO.getExpiredTime());
        setInstanceChargeType(ecsServerDO.getInstanceChargeType());
        setIoOptimized(ecsServerDO.isIoOptimized());
        setCreationTime(ecsServerDO.getCreationTime());
    }

    private EcsPropertyDO networkTypePropertyDO;

    private EcsPropertyDO imagePropertyDO;

    private EcsPropertyDO vpcPropertyDO;

    private EcsPropertyDO vswitchPropertyDO;

    private EcsPropertyDO securityGroupPropertyDO;

    private int envType;

    private int serverType;

    private int useType;

    private String serialNumber;

    /**
     * 过期天数
     */
    private long expiredDay;

}
