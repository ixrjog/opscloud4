package com.sdg.cmdb.domain.aliyun;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/13.
 */
public class AliyunVswitchVO implements Serializable {
    private static final long serialVersionUID = 7149493023344386698L;

    private long id;

    private long vpcId;

    private String vswitchDesc;

    public AliyunVswitchVO() {
    }

    public AliyunVswitchVO(AliyunVswitchDO aliyunVswitchDO) {
        this.id = aliyunVswitchDO.getId();
        this.vpcId = aliyunVswitchDO.getVpcId();
        this.vswitchDesc = aliyunVswitchDO.getVswitchDesc();
    }

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

    public String getVswitchDesc() {
        return vswitchDesc;
    }

    public void setVswitchDesc(String vswitchDesc) {
        this.vswitchDesc = vswitchDesc;
    }

    @Override
    public String toString() {
        return "AliyunVswitchVO{" +
                "id=" + id +
                ", vpcId=" + vpcId +
                ", vswitchDesc='" + vswitchDesc + '\'' +
                '}';
    }



}
