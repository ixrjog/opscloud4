package com.baiyi.caesar.ldap.convert;

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

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Person person) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(person.getUsername()) // 资产id = username
                .name(person.getDisplayName())
                .assetKey(person.getUsername())
                .assetKey2(person.getEmail())
                .assetType("USER")
                .kind("user")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("mobile", person.getMobile())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Group group) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(group.getGroupName()) // 资产id = 组名
                .name(group.getGroupName())
                .assetKey(group.getGroupName())
                .assetType("GROUP")
                .kind("userGroup")
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
