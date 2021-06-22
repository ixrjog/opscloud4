package com.baiyi.caesar.ldap.convert;

import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.builder.AssetContainerBuilder;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.ldap.entry.Group;
import com.baiyi.caesar.ldap.entry.Person;

/**
 * @Author baiyi
 * @Date 2021/6/18 3:31 下午
 * @Version 1.0
 */
public class LdapAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Person entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getUsername()) // 资产id = username
                .name(entry.getDisplayName())
                .assetKey(entry.getUsername())
                .assetKey2(entry.getEmail())
                .assetType(DsAssetTypeEnum.USER.name())
                .kind("user")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("mobile", entry.getMobile())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Group entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getGroupName()) // 资产id = 组名
                .name(entry.getGroupName())
                .assetKey(entry.getGroupName())
                .assetType(DsAssetTypeEnum.GROUP.name())
                .kind("userGroup")
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
