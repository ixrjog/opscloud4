package com.baiyi.opscloud.cloud.vpc.builder;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/19 11:04 上午
 * @Version 1.0
 */
@Data
@Builder
public class OcCloudVpcVswitchBO {
    private Integer id;
    private String regionId;
    private String zoneId;
    private String vpcId;
    private String vswitchName;
    private String vswitchId;
    private String vswitchStatus;
    private String cidrBlock;
    private Integer cloudType;
    private String description;
    private Date creationTime;
    private Integer availableIpAddressCount;
    @Builder.Default
    private Integer isActive = 0;
    private Date createTime;
    private Date updateTime;
    private String comment;
}
