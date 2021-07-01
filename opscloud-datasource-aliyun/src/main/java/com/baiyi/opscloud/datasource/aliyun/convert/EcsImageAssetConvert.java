package com.baiyi.opscloud.datasource.aliyun.convert;

import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.datasource.builder.AssetContainer;
import com.baiyi.opscloud.datasource.builder.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

import static com.baiyi.opscloud.datasource.aliyun.convert.ComputeAssetConvert.toGmtDate;

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
