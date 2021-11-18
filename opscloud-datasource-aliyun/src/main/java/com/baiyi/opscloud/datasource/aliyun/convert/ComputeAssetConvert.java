package com.baiyi.opscloud.datasource.aliyun.convert;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.CollectionUtils;
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
        return TimeUtil.toGmtDate(time, TimeZoneEnum.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeInstancesResponse.Instance entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getInstanceId()) // 资产id = 实例id
                .name(entity.getInstanceName())
                // priveteIp
                .assetKey(entity.getInstanceNetworkType().equals(VPC) ? entity.getVpcAttributes().getPrivateIpAddress().get(0) :
                        entity.getInnerIpAddress().get(0))
                // publicIp
                .assetKey2(getPublicIp(entity))
                .assetType(DsAssetTypeEnum.ECS.name())
                .kind(entity.getInstanceType())
                .regionId(entity.getRegionId())
                .zone(entity.getZoneId())
                .createdTime(toGmtDate(entity.getCreationTime()))
                .expiredTime(
                        entity.getInstanceChargeType().equalsIgnoreCase(PRE_PAID) && !StringUtils.isEmpty(entity.getExpiredTime())
                                ? toGmtDate(entity.getExpiredTime()) : null)
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("cpu", entity.getCpu())
                .paramProperty("vpcId", entity.getVpcAttributes() != null ? entity.getVpcAttributes().getVpcId() : "")
                .paramProperty("memory", entity.getMemory())
                .paramProperty("chargeType", entity.getInstanceChargeType())
                .paramProperty("imageId", entity.getImageId())
                .paramProperty("osName", entity.getOSName())
                .paramProperty("osType", entity.getOSType())
                .build();
    }

    private static String getPublicIp(DescribeInstancesResponse.Instance entity) {
        if (!CollectionUtils.isEmpty(entity.getPublicIpAddress()))
            return entity.getPublicIpAddress().get(0);
        if (entity.getEipAddress() != null)
            return entity.getEipAddress().getIpAddress();
        return Strings.EMPTY;
    }
}
