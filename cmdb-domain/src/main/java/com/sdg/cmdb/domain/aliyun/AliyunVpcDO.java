package com.sdg.cmdb.domain.aliyun;

import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/13.
 */
public class AliyunVpcDO implements Serializable {
    private static final long serialVersionUID = 8598709051367295403L;

    private long id;

    private long networkId = 1;

    private String aliyunVpcId;

    private String vpcDesc;

    private String gmtCreate;

    private String gmtModify;


    public AliyunVpcDO() {

    }

    public AliyunVpcDO(DescribeVpcsResponse.Vpc vpc) {
        this.aliyunVpcId = vpc.getVpcId();

        if (!StringUtils.isEmpty(vpc.getVpcName())) {
            this.vpcDesc = vpc.getVpcName();
        } else {
            this.vpcDesc = vpc.getDescription();
        }
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

    public String getAliyunVpcId() {
        return aliyunVpcId;
    }

    public void setAliyunVpcId(String aliyunVpcId) {
        this.aliyunVpcId = aliyunVpcId;
    }

    public String getVpcDesc() {
        return vpcDesc;
    }

    public void setVpcDesc(String vpcDesc) {
        this.vpcDesc = vpcDesc;
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
        return "AliyunVpcDO{" +
                "id=" + id +
                ", networkId=" + networkId +
                ", aliyunVpcId='" + aliyunVpcId + '\'' +
                ", vpcDesc='" + vpcDesc + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }


}
