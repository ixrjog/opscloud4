package com.sdg.cmdb.domain.aliyun;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liangjian on 2017/6/13.
 */
public class AliyunVpcVO implements Serializable {
    private static final long serialVersionUID = -3675204454130244555L;

    private long id;

    private long networkId;

    private String aliyunVpcId;

    private String vpcDesc;

    private List<AliyunVswitchDO> vSwitchs;

    private List<AliyunVpcSecurityGroupDO> securityGroups;

    public AliyunVpcVO() {

    }

    public AliyunVpcVO(AliyunVpcDO aliyunVpcDO) {
        this.id = aliyunVpcDO.getId();
        this.networkId = aliyunVpcDO.getNetworkId();
        this.vpcDesc = aliyunVpcDO.getVpcDesc();
        this.aliyunVpcId = aliyunVpcDO.getAliyunVpcId();
    }

    public AliyunVpcVO(AliyunVpcDO aliyunVpcDO,List<AliyunVswitchDO> vSwitchs,List<AliyunVpcSecurityGroupDO> securityGroups) {
        this.id = aliyunVpcDO.getId();
        this.networkId = aliyunVpcDO.getNetworkId();
        this.vpcDesc = aliyunVpcDO.getVpcDesc();
        this.aliyunVpcId = aliyunVpcDO.getAliyunVpcId();
        this.vSwitchs = vSwitchs;
        this.securityGroups = securityGroups;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(long networkId) {
        this.networkId = networkId;
    }

    public String getVpcDesc() {
        return vpcDesc;
    }

    public String getAliyunVpcId() {
        return aliyunVpcId;
    }

    public void setAliyunVpcId(String aliyunVpcId) {
        this.aliyunVpcId = aliyunVpcId;
    }

    public void setVpcDesc(String vpcDesc) {
        this.vpcDesc = vpcDesc;
    }

    public List<AliyunVswitchDO> getvSwitchs() {
        return vSwitchs;
    }

    public void setvSwitchs(List<AliyunVswitchDO> vSwitchs) {
        this.vSwitchs = vSwitchs;
    }

    public List<AliyunVpcSecurityGroupDO> getSecurityGroups() {
        return securityGroups;
    }

    public void setSecurityGroups(List<AliyunVpcSecurityGroupDO> securityGroups) {
        this.securityGroups = securityGroups;
    }

    @Override
    public String toString() {
        return "AliyunVpcVO{" +
                "id=" + id +
                ", networkId=" + networkId +
                ", vpcDesc='" + vpcDesc + '\'' +
                '}';
    }


}
