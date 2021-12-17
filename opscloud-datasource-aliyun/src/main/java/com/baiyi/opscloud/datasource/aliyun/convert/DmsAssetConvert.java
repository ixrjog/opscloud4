package com.baiyi.opscloud.datasource.aliyun.convert;

import com.baiyi.opscloud.datasource.aliyun.dms.entity.DmsUser;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;

/**
 * @Author baiyi
 * @Date 2021/12/16 4:24 PM
 * @Version 1.0
 */
public class DmsAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DmsUser.User entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getUserId())
                .name(entity.getNickName())
                .assetKey(entity.getUid())
                .assetKey2(entity.getParentUid())
                .kind("user")
                .assetType(DsAssetTypeEnum.DMS_USER.name())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("mobile", entity.getMobile())
                .paramProperty("notificationMode", entity.getNotificationMode())
                .paramProperty("email", entity.getEmail())
                .build();
    }

    public static DmsUser.User toDmsUser(DatasourceInstanceAsset asset, String mobile) {
        return DmsUser.User.builder()
                .nickName(asset.getName())
                .mobile(mobile)
                .uid(asset.getAssetId())
                .build();
    }

}
