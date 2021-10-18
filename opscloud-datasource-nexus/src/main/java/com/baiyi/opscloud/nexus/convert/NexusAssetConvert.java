package com.baiyi.opscloud.nexus.convert;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.nexus.entry.NexusAsset;

/**
 * @Author baiyi
 * @Date 2021/10/18 10:59 上午
 * @Version 1.0
 */
public class NexusAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, NexusAsset.Item entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getId())
                .name(entry.getDownloadUrl())
                .assetKey(entry.getDownloadUrl())
                .assetKey2(entry.getRepository())
                .isActive(true)
                .assetType(DsAssetTypeEnum.NEXUS_ASSET.name())
                .kind("user")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("md5", entry.getChecksum().getMd5())
                .paramProperty("sha1", entry.getChecksum().getSha1())
                // .paramProperty("sha256", entry.getChecksum().getSha256())
                // .paramProperty("sha512", entry.getChecksum().getSha512())
                .build();
    }
}
