package com.baiyi.opscloud.cloud.server.builder;

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
public class CloudServerBO {

    private Integer id;
    private String serverName;
    private String instanceId;
    private String instanceName;
    private String regionId;
    private String zone;
    private Integer cloudServerType;
    private String privateIp;
    private String publicIp;
    private Integer cpu;
    private Integer memory;
    private String vpcId;
    private String instanceType;
    private String imageId;
    @Builder.Default
    private Integer systemDiskSize = 0;
    @Builder.Default
    private Integer dataDiskSize = 0;
    private Date createdTime;
    private Date expiredTime;
    private String chargeType;
    private Integer serverStatus;
    private String renewalStatus;
    private Integer serverId;
    @Builder.Default
    private Boolean powerMgmt = Boolean.FALSE;
    private Integer powerStatus;
    private String comment;
    private String instanceDetail;
    private Date createTime;
    private Date updateTime;
}
