package com.baiyi.opscloud.datasource.aliyun.converter;

import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/6/18 3:31 下午
 * @Version 1.0
 */
public class ComputeAssetConverter {

    private static final String VPC = "vpc";

    private static final String PRE_PAID = "PrePaid";

    public static Date toUtcDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeInstancesResponse.Instance entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 资产id = 实例id
                .assetId(entity.getInstanceId())
                .name(entity.getInstanceName())
                // privateIp
                .assetKey(entity.getInstanceNetworkType().equals(VPC) ? entity.getVpcAttributes().getPrivateIpAddress().getFirst() :
                        entity.getInnerIpAddress().getFirst())
                // publicIp
                .assetKey2(getPublicIp(entity))
                .assetType(DsAssetTypeConstants.ECS.name())
                .kind(entity.getInstanceType())
                .regionId(entity.getRegionId())
                .zone(entity.getZoneId())
                .createdTime(toUtcDate(entity.getCreationTime()))
                .expiredTime(
                        entity.getInstanceChargeType().equalsIgnoreCase(PRE_PAID) && !StringUtils.isEmpty(entity.getExpiredTime())
                                ? toUtcDate(entity.getExpiredTime()) : null)
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
        if (!CollectionUtils.isEmpty(entity.getPublicIpAddress())) {
            return entity.getPublicIpAddress().getFirst();
        }
        if (entity.getEipAddress() != null) {
            return entity.getEipAddress().getIpAddress();
        }
        return StringUtils.EMPTY;
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeImagesResponse.Image entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 资产id = 实例id
                .assetId(entity.getImageId())
                .name(entity.getImageName())
                .assetKey(entity.getImageId())
                .kind("ecsImage")
                .assetType(DsAssetTypeConstants.ECS_IMAGE.name())
                .description(entity.getDescription())
                .createdTime(toUtcDate(entity.getCreationTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("oSType", entity.getOSType())
                .paramProperty("oSName", entity.getOSName())
                .paramProperty("size",entity.getSize())
                .build();
    }

}