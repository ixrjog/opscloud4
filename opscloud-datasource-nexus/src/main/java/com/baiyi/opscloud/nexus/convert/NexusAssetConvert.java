package com.baiyi.opscloud.nexus.convert;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.nexus.entity.NexusAsset;

/**
 * @Author baiyi
 * @Date 2021/10/18 10:59 上午
 * @Version 1.0
 */
public class NexusAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, NexusAsset.Item entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getId())
                .name(entity.getDownloadUrl())
                .assetKey(entity.getDownloadUrl())
                .assetKey2(entity.getRepository())
                .isActive(true)
                .assetType(DsAssetTypeEnum.NEXUS_ASSET.name())
                .kind("user")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("md5", entity.getChecksum().getMd5())
                .paramProperty("sha1", entity.getChecksum().getSha1())
                .build();
    }
}
