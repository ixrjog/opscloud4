package com.baiyi.opscloud.datasource.aliyun.converter;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

import static com.baiyi.opscloud.datasource.aliyun.converter.ComputeAssetConverter.toUtcDate;

/**
 * @Author 修远
 * @Date 2021/6/23 1:28 下午
 * @Since 1.0
 */
public class VpcAssetConverter {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeVpcsResponse.Vpc entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 资产id = 实例id
                .assetId(entity.getVpcId())
                .name(entity.getVpcName())
                .assetKey(entity.getVpcId())
                // cidrBlock
                .assetKey2(entity.getCidrBlock())
                .kind("aliyunVpc")
                .assetType(DsAssetTypeConstants.VPC.name())
                .regionId(entity.getRegionId())
                .description(entity.getDescription())
                .createdTime(TimeUtil.toDate(entity.getCreationTime(), TimeZoneEnum.UTC))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("isDefault", entity.getIsDefault())
                .paramProperty("vRouterId", entity.getVRouterId())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeSecurityGroupsResponse.SecurityGroup entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 资产id = 实例id
                .assetId(entity.getSecurityGroupId())
                .name(entity.getSecurityGroupName())
                .assetKey(entity.getSecurityGroupId())
                .kind("aliyunSecurityGroup")
                .assetType(DsAssetTypeConstants.ECS_SG.name())
                .description(entity.getDescription())
                .createdTime(TimeUtil.toDate(entity.getCreationTime(), TimeZoneEnum.UTC))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("securityGroupType", entity.getSecurityGroupType())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeVSwitchesResponse.VSwitch entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 资产id = 实例id
                .assetId(entity.getVSwitchId())
                .name(entity.getVSwitchName())
                .assetKey(entity.getVSwitchId())
                // cidrBlock
                .assetKey2(entity.getCidrBlock())
                .kind("aliyunVSwitch")
                .assetType(DsAssetTypeConstants.V_SWITCH.name())
                .zone(entity.getZoneId())
                .description(entity.getDescription())
                .createdTime(toUtcDate(entity.getCreationTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("isDefault", entity.getIsDefault())
                .build();
    }

}