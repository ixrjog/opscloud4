package com.baiyi.opscloud.cloud.vpc.builder;

import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.baiyi.opscloud.cloud.account.CloudAccount;
import com.baiyi.opscloud.common.base.CloudType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpc;

/**
 * @Author baiyi
 * @Date 2020/3/19 9:35 上午
 * @Version 1.0
 */
public class OcCloudVpcBuilder {

    /**
     * aliyun vpc
     * @param account
     * @return
     */
    public static OcCloudVpc build(CloudAccount account, DescribeVpcsResponse.Vpc vpc) {
        OcCloudVpcBO ocCloudVpcBO = OcCloudVpcBO.builder()
                .uid(account.getUid())
                .accountName(account.getName())
                .regionId(vpc.getRegionId())
                .vpcId(vpc.getVpcId())
                .vpcName(vpc.getVpcName())
                .cidrBlock(vpc.getCidrBlock())
                .cloudType(CloudType.ALIYUN.getType())
                .description(vpc.getDescription())
                .creationTime(TimeUtils.acqGmtDate(vpc.getCreationTime()))
                .build();
        return covert(ocCloudVpcBO);
    }

    private static OcCloudVpc covert(OcCloudVpcBO ocCloudVpcBO) {
        return BeanCopierUtils.copyProperties(ocCloudVpcBO, OcCloudVpc.class);
    }
}
