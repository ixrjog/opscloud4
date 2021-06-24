package com.baiyi.caesar.datasource.aliyun.convert;

import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.builder.AssetContainerBuilder;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;

import static com.baiyi.caesar.datasource.aliyun.convert.ComputeAssetConvert.toGmtDate;

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
}
