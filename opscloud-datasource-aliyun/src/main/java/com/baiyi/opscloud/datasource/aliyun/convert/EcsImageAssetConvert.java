package com.baiyi.opscloud.datasource.aliyun.convert;

import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

import static com.baiyi.opscloud.datasource.aliyun.convert.ComputeAssetConvert.toGmtDate;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/23 3:45 下午
 * @Since 1.0
 */
public class EcsImageAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeImagesResponse.Image entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getImageId()) // 资产id = 实例id
                .name(entity.getImageName())
                .assetKey(entity.getImageId())
                .kind("ecsImage")
                .assetType(DsAssetTypeEnum.ECS_IMAGE.name())
                .description(entity.getDescription())
                .createdTime(toGmtDate(entity.getCreationTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("oSType", entity.getOSType())
                .paramProperty("oSName", entity.getOSName())
                .paramProperty("size",entity.getSize())
                .build();
    }
}
