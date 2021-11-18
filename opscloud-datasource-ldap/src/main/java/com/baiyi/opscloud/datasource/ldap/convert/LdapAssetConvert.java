package com.baiyi.opscloud.datasource.ldap.convert;

import com.baiyi.opscloud.datasource.ldap.entity.Group;
import com.baiyi.opscloud.datasource.ldap.entity.Person;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

/**
 * @Author baiyi
 * @Date 2021/6/18 3:31 下午
 * @Version 1.0
 */
public class LdapAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Person entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getUsername()) // 资产id = username
                .name(entity.getDisplayName())
                .assetKey(entity.getUsername())
                .assetKey2(entity.getEmail())
                .assetType(DsAssetTypeEnum.USER.name())
                .kind("user")
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("mobile", entity.getMobile())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Group entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getGroupName()) // 资产id = 组名
                .name(entity.getGroupName())
                .assetKey(entity.getGroupName())
                .assetType(DsAssetTypeEnum.GROUP.name())
                .kind("userGroup")
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
