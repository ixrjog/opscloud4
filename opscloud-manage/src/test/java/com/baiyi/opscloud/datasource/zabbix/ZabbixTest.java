package com.baiyi.opscloud.datasource.zabbix;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
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
