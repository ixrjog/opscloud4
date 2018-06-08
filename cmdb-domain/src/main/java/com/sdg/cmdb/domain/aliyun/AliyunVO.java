package com.sdg.cmdb.domain.aliyun;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/14.
 */
public class AliyunVO implements Serializable {
    private static final long serialVersionUID = -8812883362975142373L;

    private AliyunEcsImageDO aliyunEcsImageDO;

    private AliyunNetworkDO aliyunNetworkDO;

    private AliyunVpcDO aliyunVpcDO;

    private AliyunVpcSecurityGroupDO aliyunVpcSecurityGroupDO;

    private AliyunVswitchDO aliyunVswitchDO;

    public AliyunEcsImageDO getAliyunEcsImageDO() {
        return aliyunEcsImageDO;
    }

    public void setAliyunEcsImageDO(AliyunEcsImageDO aliyunEcsImageDO) {
        this.aliyunEcsImageDO = aliyunEcsImageDO;
    }

    public AliyunNetworkDO getAliyunNetworkDO() {
        return aliyunNetworkDO;
    }

    public void setAliyunNetworkDO(AliyunNetworkDO aliyunNetworkDO) {
        this.aliyunNetworkDO = aliyunNetworkDO;
    }

    public AliyunVpcDO getAliyunVpcDO() {
        return aliyunVpcDO;
    }

    public void setAliyunVpcDO(AliyunVpcDO aliyunVpcDO) {
        this.aliyunVpcDO = aliyunVpcDO;
    }

    public AliyunVpcSecurityGroupDO getAliyunVpcSecurityGroupDO() {
        return aliyunVpcSecurityGroupDO;
    }

    public void setAliyunVpcSecurityGroupDO(AliyunVpcSecurityGroupDO aliyunVpcSecurityGroupDO) {
        this.aliyunVpcSecurityGroupDO = aliyunVpcSecurityGroupDO;
    }

    public AliyunVswitchDO getAliyunVswitchDO() {
        return aliyunVswitchDO;
    }

    public void setAliyunVswitchDO(AliyunVswitchDO aliyunVswitchDO) {
        this.aliyunVswitchDO = aliyunVswitchDO;
    }
}
