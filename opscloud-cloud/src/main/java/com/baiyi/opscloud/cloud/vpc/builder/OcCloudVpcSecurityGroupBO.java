package com.baiyi.opscloud.cloud.vpc.builder;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/19 1:44 下午
 * @Version 1.0
 */
@Data
@Builder
public class OcCloudVpcSecurityGroupBO {

    private Integer id;
    private String regionId;
    private String vpcId;
    private String securityGroupName;
    private String securityGroupId;
    private String securityGroupType;
    private Integer cloudType;
    private String description;
    private Date creationTime;
    @Builder.Default
    private Integer isActive = 0;
    private Date createTime;
    private Date updateTime;
    private String comment;

}
