package com.baiyi.caesar.datasource.aliyun.convert;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.caesar.datasource.util.TimeUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/6/18 3:31 下午
 * @Version 1.0
 */
public class ComputeAssetConvert {

    private static final String VPC = "vpc";

    private static final String PRE_PAID = "PrePaid";

    public static Date toGmtDate(String time) {
        return TimeUtil.toGmtDate(time, TimeUtil.Format.UTC);
    }

    public static DatasourceInstanceAsset toAsset(DatasourceInstance dsInstance, DescribeInstancesResponse.Instance instance) {
        return DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(instance.getInstanceId()) // 资产id = 实例id
                .name(instance.getInstanceName())
                // priveteIp
                .assetKey(instance.getInstanceNetworkType().equals(VPC) ? instance.getVpcAttributes().getPrivateIpAddress().get(0) :
                        instance.getInnerIpAddress().get(0))
                // publicIp
                .assetKey2(instance.getPublicIpAddress().size() != 0 ? instance.getPublicIpAddress().get(0) : "")
                .assetType("ECS")
                .kind(instance.getInstanceType())
                .regionId(instance.getRegionId())
                .zone(instance.getZoneId())
                .createdTime(toGmtDate(instance.getCreationTime()))
                .expiredTime(
                        instance.getInstanceChargeType().equalsIgnoreCase(PRE_PAID) && !StringUtils.isEmpty(instance.getExpiredTime())
                                ? toGmtDate(instance.getExpiredTime()) : null)
                .build();
    }
}
