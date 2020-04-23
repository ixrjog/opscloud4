package com.baiyi.opscloud.cloud.vpc.builder;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.baiyi.opscloud.common.base.CloudType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpc;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcVswitch;

/**
 * @Author baiyi
 * @Date 2020/3/19 11:04 上午
 * @Version 1.0
 */
public class OcCloudVpcVswitchBuilder {

    public static OcCloudVpcVswitch build(OcCloudVpc vpc, DescribeVSwitchesResponse.VSwitch vsw) {
        OcCloudVpcVswitchBO ocCloudVpcVswitchBO = OcCloudVpcVswitchBO.builder()
                .vpcId(vpc.getVpcId())
                .regionId(vpc.getRegionId())
                .vswitchId(vsw.getVSwitchId())
                .vswitchName(vsw.getVSwitchName())
                .vswitchStatus(vsw.getStatus())
                .cidrBlock(vsw.getCidrBlock())
                .cloudType(CloudType.ALIYUN.getType())
                .zoneId(vsw.getZoneId())
                .description(vsw.getDescription())
                .creationTime(TimeUtils.acqGmtDate(vsw.getCreationTime()))
                .availableIpAddressCount(vsw.getAvailableIpAddressCount().intValue())
                .build();
        return covert(ocCloudVpcVswitchBO);
    }

    private static OcCloudVpcVswitch covert(OcCloudVpcVswitchBO ocCloudVpcVswitchBO) {
        return BeanCopierUtils.copyProperties(ocCloudVpcVswitchBO, OcCloudVpcVswitch.class);
    }
}
