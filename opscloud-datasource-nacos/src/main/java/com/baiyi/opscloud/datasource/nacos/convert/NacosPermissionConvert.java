package com.baiyi.opscloud.datasource.nacos.convert;

import com.baiyi.opscloud.datasource.nacos.entity.NacosPermission;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2021/11/12 4:18 下午
 * @Version 1.0
 */
public class NacosPermissionConvert {

    private static String buildAssetKey(NacosPermission.Permission entity){
        return Joiner.on("#").join(entity.getRole(),entity.getResource(),entity.getAction());
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, NacosPermission.Permission entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getRole())
                .name(entity.getRole())
                .assetKey(buildAssetKey(entity))
                .isActive(true)
                .assetType(DsAssetTypeEnum.NACOS_PERMISSION.name())
                .kind("permission")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("action", entity.getAction())
                .paramProperty("resource", entity.getResource())
                .build();
    }

}
