package com.baiyi.opscloud.cloudserver.builder;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/1/10 11:26 上午
 * @Version 1.0
 */
@Data
@Builder
public class OcCloudserverBO {

    private Integer id;
    private String serverName;
    private String instanceId;
    private String instanceName;
    private String zone;
    private Integer cloudserverType;
    private String privateIp;
    private String publicIp;
    private Integer cpu;
    private Integer memory;
    private String vpcId;
    private String instanceType;
    private String imageId;
    private Integer systemDiskSize;
    private Integer dataDiskSize;
    private Date createdTime;
    private Date expiredTime;
    private String chargeType;
    private Integer serverStatus;
    private String renewalStatus;
    private Integer serverId;
    @Builder.Default
    private Boolean powerMgmt = Boolean.FALSE;
    private String comment;
    private String instanceDetail;
    private Date createTime;
    private Date updateTime;
}
