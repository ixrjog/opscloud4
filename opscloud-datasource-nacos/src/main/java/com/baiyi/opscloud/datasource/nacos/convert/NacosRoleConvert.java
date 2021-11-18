package com.baiyi.opscloud.datasource.nacos.convert;

import com.baiyi.opscloud.datasource.nacos.entity.NacosRole;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;

/**
 * @Author baiyi
 * @Date 2021/11/15 3:58 下午
 * @Version 1.0
 */
public class NacosRoleConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, NacosRole.Role entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getUsername())
                .name(entity.getUsername().replace("LDAP_",""))
                .assetKey(entity.getUsername())
                .assetKey2(entity.getRole())
                .isActive(true)
                .assetType(DsAssetTypeEnum.NACOS_USER.name())
                .kind("user")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }

}
