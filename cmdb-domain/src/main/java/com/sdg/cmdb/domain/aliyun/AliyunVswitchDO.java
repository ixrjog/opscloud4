package com.sdg.cmdb.domain.aliyun;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/13.
 */
public class AliyunVswitchDO implements Serializable {
    private static final long serialVersionUID = 7142806229507163472L;

    private long id;

    private long vpcId;

    private String vswitchId;

    private String vswitchDesc;

    private String gmtCreate;

    private String gmtModify;

    public AliyunVswitchDO(){

    }

    public AliyunVswitchDO(AliyunVpcDO aliyunVpcDO,DescribeVSwitchesResponse.VSwitch vSwitch){
        this.vpcId = aliyunVpcDO.getId();
        this.vswitchId = vSwitch.getVSwitchId();
        if(!StringUtils.isEmpty(vSwitch.getVSwitchName())){
            this.vswitchDesc = vSwitch.getVSwitchName();
        }else{
            this.vswitchDesc = vSwitch.getDescription();
        }
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

    public String getVswitchId() {
        return vswitchId;
    }

    public void setVswitchId(String vswitchId) {
        this.vswitchId = vswitchId;
    }

    public String getVswitchDesc() {
        return vswitchDesc;
    }

    public void setVswitchDesc(String vswitchDesc) {
        this.vswitchDesc = vswitchDesc;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    @Override
    public String toString() {
        return "AliyunVswitchDO{" +
                "id=" + id +
                ", vpcId=" + vpcId +
                ", vswitchId='" + vswitchId + '\'' +
                ", vswitchDesc='" + vswitchDesc + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }




}
