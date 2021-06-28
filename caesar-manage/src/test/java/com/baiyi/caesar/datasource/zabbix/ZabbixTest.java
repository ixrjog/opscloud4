日常开发	f4f8a7a	baiyi <baiyi@xinc818.group>	2021年6月28日 下午6:15
        package com.baiyi.caesar.datasource.zabbix;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.datasource.provider.base.asset.SimpleAssetProvider;
import org.junit.jupiter.api.Test;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 3:54 下午
 * @Since 1.0
 */
public class ZabbixTest extends BaseUnit {

    @Test
    void pullUserTest() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ZABBIX.getName(), DsAssetTypeEnum.ZABBIX_USER.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(4);
    }


    @Test
    void pullUserGroupTest() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.ZABBIX.getName(), DsAssetTypeEnum.ZABBIX_USER_GROUP.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(4);
    }
}
