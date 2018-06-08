package com.sdg.cmdb.domain.server;

import com.sdg.cmdb.domain.aliyun.AliyunVO;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/6.
 */
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

    public EcsPropertyDO getNetworkTypePropertyDO() {
        return networkTypePropertyDO;
    }

    public void setNetworkTypePropertyDO(EcsPropertyDO networkTypePropertyDO) {
        this.networkTypePropertyDO = networkTypePropertyDO;
    }

    public EcsPropertyDO getImagePropertyDO() {
        return imagePropertyDO;
    }

    public void setImagePropertyDO(EcsPropertyDO imagePropertyDO) {
        this.imagePropertyDO = imagePropertyDO;
    }

    public EcsPropertyDO getVpcPropertyDO() {
        return vpcPropertyDO;
    }

    public void setVpcPropertyDO(EcsPropertyDO vpcPropertyDO) {
        this.vpcPropertyDO = vpcPropertyDO;
    }

    public EcsPropertyDO getVswitchPropertyDO() {
        return vswitchPropertyDO;
    }

    public void setVswitchPropertyDO(EcsPropertyDO vswitchPropertyDO) {
        this.vswitchPropertyDO = vswitchPropertyDO;
    }

    public EcsPropertyDO getSecurityGroupPropertyDO() {
        return securityGroupPropertyDO;
    }

    public void setSecurityGroupPropertyDO(EcsPropertyDO securityGroupPropertyDO) {
        this.securityGroupPropertyDO = securityGroupPropertyDO;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
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
}
