package com.baiyi.caesar.datasource.aliyun.convert;

import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.builder.AssetContainerBuilder;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;

import static com.baiyi.caesar.datasource.aliyun.convert.ComputeAssetConvert.toGmtDate;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/23 3:45 下午
 * @Since 1.0
 */
public class EcsImageAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeImagesResponse.Image entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getImageId()) // 资产id = 实例id
                .name(entry.getImageName())
                .assetKey(entry.getImageId())
                .kind("ecsImage")
                .assetType(DsAssetTypeEnum.ECS_IMAGE.name())
                .description(entry.getDescription())
                .createdTime(toGmtDate(entry.getCreationTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("oSType", entry.getOSType())
                .paramProperty("oSName", entry.getOSName())
                .paramProperty("size",entry.getSize())
                .build();
    }
}
