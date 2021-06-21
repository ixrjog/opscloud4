package com.baiyi.caesar.datasource.account;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.asset.AbstractAssetRelationProvider;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.ldap.entry.Group;
import com.baiyi.caesar.ldap.entry.Person;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/6/10 1:50 下午
 * @Version 1.0
 */
public class AccountTest extends BaseUnit {

    @Test
    void pullAccount() {
        AbstractAssetRelationProvider<Person, Group> assetProvider = AssetProviderFactory.getAssetRelationProvider(DsTypeEnum.LDAP.getName(), DsAssetTypeEnum.USER.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(1);
    }

    @Test
    void pullGroup() {
        AbstractAssetRelationProvider<Group, Person> assetProvider = AssetProviderFactory.getAssetRelationProvider(DsTypeEnum.LDAP.getName(), DsAssetTypeEnum.GROUP.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(1);
    }

}
