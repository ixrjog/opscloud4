package com.baiyi.opscloud.cloud.vpc.builder;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.baiyi.opscloud.common.base.CloudType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpc;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcSecurityGroup;

/**
 * @Author baiyi
 * @Date 2020/3/19 1:46 下午
 * @Version 1.0
 */
public class OcCloudVpcSecurityGroupBuilder {


    public static OcCloudVpcSecurityGroup build(OcCloudVpc vpc, DescribeSecurityGroupsResponse.SecurityGroup sg) {
        OcCloudVpcSecurityGroupBO ocCloudVpcSecurityGroupBO = OcCloudVpcSecurityGroupBO.builder()
                .vpcId(vpc.getVpcId())
                .regionId(vpc.getRegionId())
                 .securityGroupId(sg.getSecurityGroupId())
                .securityGroupName(sg.getSecurityGroupName())
                .securityGroupType(sg.getSecurityGroupType())
                .cloudType(CloudType.ALIYUN.getType())
                .creationTime(TimeUtils.acqGmtDate(sg.getCreationTime()))
                .build();
        return covert(ocCloudVpcSecurityGroupBO);
    }

    private static OcCloudVpcSecurityGroup covert(OcCloudVpcSecurityGroupBO ocCloudVpcSecurityGroupBO) {
        return BeanCopierUtils.copyProperties(ocCloudVpcSecurityGroupBO, OcCloudVpcSecurityGroup.class);
    }
}
