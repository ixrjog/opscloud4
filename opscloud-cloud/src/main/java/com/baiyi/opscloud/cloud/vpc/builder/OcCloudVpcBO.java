package com.baiyi.opscloud.cloud.vpc.builder;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/19 9:36 上午
 * @Version 1.0
 */
@Builder
@Data
public class OcCloudVpcBO {

    private Integer id;
    private String uid;
    private String accountName;
    private String regionId;
    private String vpcId;
    private String vpcName;
    private String cidrBlock;
    private Integer cloudType;
    private String description;
    private Date creationTime;
    @Builder.Default
    private Integer isActive = 0;
    @Builder.Default
    private Integer isDeleted = 0;
    private Date createTime;
    private Date updateTime;
    private String comment;

}
