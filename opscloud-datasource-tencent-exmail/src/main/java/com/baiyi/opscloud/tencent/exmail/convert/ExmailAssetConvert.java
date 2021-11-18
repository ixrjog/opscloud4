package com.baiyi.opscloud.tencent.exmail.convert;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.tencent.exmail.entity.ExmailUser;

/**
 * @Author baiyi
 * @Date 2021/10/14 11:13 上午
 * @Version 1.0
 */
public class ExmailAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ExmailUser entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getUserId())
                .name(entity.getName())
                .assetKey(entity.getUserId())
                .assetKey2(entity.getUserId().split("@")[0])  // 用户名
                .description(entity.getPosition())
                .isActive("1".equals(entity.getEnable()))
                .assetType(DsAssetTypeEnum.TENCENT_EXMAIL_USER.name())
                .kind("user")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("mobile", entity.getMobile())
                .build();
    }
}
