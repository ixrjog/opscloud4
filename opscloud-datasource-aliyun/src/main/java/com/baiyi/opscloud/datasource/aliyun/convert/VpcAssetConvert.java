package com.baiyi.opscloud.datasource.aliyun.convert;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

import static com.baiyi.opscloud.datasource.aliyun.convert.ComputeAssetConvert.toGmtDate;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/23 1:28 下午
 * @Since 1.0
 */
public class VpcAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeVpcsResponse.Vpc entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getVpcId()) // 资产id = 实例id
                .name(entry.getVpcName())
                .assetKey(entry.getVpcId())
                // cidrBlock
                .assetKey2(entry.getCidrBlock())
                .kind("aliyunVpc")
                .assetType(DsAssetTypeEnum.VPC.name())
                .regionId(entry.getRegionId())
                .description(entry.getDescription())
                .createdTime(toGmtDate(entry.getCreationTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("isDefault", entry.getIsDefault())
                .paramProperty("vRouterId", entry.getVRouterId())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeSecurityGroupsResponse.SecurityGroup entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getSecurityGroupId()) // 资产id = 实例id
                .name(entry.getSecurityGroupName())
                .assetKey(entry.getSecurityGroupId())
                .kind("aliyunSecurityGroup")
                .assetType(DsAssetTypeEnum.ECS_SG.name())
                .description(entry.getDescription())
                .createdTime(toGmtDate(entry.getCreationTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("securityGroupType", entry.getSecurityGroupType())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeVSwitchesResponse.VSwitch entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getVSwitchId()) // 资产id = 实例id
                .name(entry.getVSwitchName())
                .assetKey(entry.getVSwitchId())
                // cidrBlock
                .assetKey2(entry.getCidrBlock())
                .kind("aliyunVSwitch")
                .assetType(DsAssetTypeEnum.V_SWITCH.name())
                .zone(entry.getZoneId())
                .description(entry.getDescription())
                .createdTime(toGmtDate(entry.getCreationTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("isDefault", entry.getIsDefault())
                .build();
    }
}
