package com.sdg.cmdb.domain.aliyun;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/13.
 */
public class AliyunVpcSecurityGroupDO implements Serializable {
    private static final long serialVersionUID = -2065681161812988909L;

    private long id;

    private long vpcId;

    private String securityGroupId;

    private String securityGroupDesc;

    private String gmtCreate;

    private String gmtModify;


    public AliyunVpcSecurityGroupDO(){

    }

    public AliyunVpcSecurityGroupDO(AliyunVpcDO aliyunVpcDO, DescribeSecurityGroupsResponse.SecurityGroup securityGroup){
        this.vpcId = aliyunVpcDO.getId();
        this.securityGroupId = securityGroup.getSecurityGroupId();
        if(!StringUtils.isEmpty(securityGroup.getSecurityGroupName())){
            this.securityGroupDesc = securityGroup.getSecurityGroupName();
        }else
            this.securityGroupDesc = securityGroup.getDescription();
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

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public String getSecurityGroupDesc() {
        return securityGroupDesc;
    }

    public void setSecurityGroupDesc(String securityGroupDesc) {
        this.securityGroupDesc = securityGroupDesc;
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
        return "AliyunVpcSecurityGroupDO{" +
                "id=" + id +
                ", vpcId=" + vpcId +
                ", securityGroupId='" + securityGroupId + '\'' +
                ", securityGroupDesc='" + securityGroupDesc + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }



}
