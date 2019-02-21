package com.sdg.cmdb.domain.server;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建ECS缓存数据
 */
@Data
public class CreateECSCache implements Serializable {
    private static final long serialVersionUID = 6606715820177437639L;


    /**
     *
     chargeType: "PrePaid"

     imageId: 7
     networkType: "vpc"
     securityGroupId: 117
     vpcId: 9
     vswitchId: 158
     */

    /**
     * 服务器登录账户
     */
    private String loginUser;
    // 付费类型
    private String chargeType;

    private long imageId;
    /**
     * 购买资源的时长，单位为：月。当 InstanceChargeType 为 PrePaid 时才生效且为必选值。取值范围：
     1 - 9
     12
     24
     36
     */
    private int period;
    //   networkType: "vpc"
    private String networkType;
    private long vpcId;
    private long vswitchId;
    private long securityGroupId;

}
