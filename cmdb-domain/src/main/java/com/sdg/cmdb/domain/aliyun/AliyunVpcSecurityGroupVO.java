package com.sdg.cmdb.domain.aliyun;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/13.
 */
public class AliyunVpcSecurityGroupVO implements Serializable {
    private static final long serialVersionUID = 6891331079472716797L;

    private long id;

    private long vpcId;

    private String securityGroupDesc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVpcId() {
        return vpcId;
    }

    public void setVpcId(long vpcId) {
        this.vpcId = vpcId;
    }

    public String getSecurityGroupDesc() {
        return securityGroupDesc;
    }

    public void setSecurityGroupDesc(String securityGroupDesc) {
        this.securityGroupDesc = securityGroupDesc;
    }

    public AliyunVpcSecurityGroupVO() {
    }

    public AliyunVpcSecurityGroupVO(AliyunVpcSecurityGroupDO aliyunVpcSecurityGroupDO) {
        this.id = aliyunVpcSecurityGroupDO.getId();
        this.vpcId = aliyunVpcSecurityGroupDO.getVpcId();
        this.securityGroupDesc = aliyunVpcSecurityGroupDO.getSecurityGroupDesc();
    }

    @Override
    public String toString() {
        return "AliyunVpcSecurityGroupVO{" +
                "id=" + id +
                ", vpcId=" + vpcId +
                ", securityGroupDesc='" + securityGroupDesc + '\'' +
                '}';
    }


}
