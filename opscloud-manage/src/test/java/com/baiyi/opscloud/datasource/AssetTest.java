package com.baiyi.opscloud.datasource;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/6/10 1:50 下午
 * @Version 1.0
 */
@Slf4j
public class AssetTest extends BaseUnit {

    @Test
    void pullAsset() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.LDAP.getName(), DsAssetTypeEnum.USER.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(1);
    }

    @Test
    void pullGroup() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.LDAP.getName(), DsAssetTypeEnum.GROUP.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(1);
    }

    @Test
    void pullAssetByGitlabUser() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.GITLAB.getName(), DsAssetTypeEnum.USER.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(2);
    }

    @Test
    void pullAssetByGitlabProject() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.GITLAB.getName(), DsAssetTypeEnum.GITLAB_PROJECT.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(2);
    }

    @Test
    void pullAssetByGitlabGroup() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.GITLAB.getName(), DsAssetTypeEnum.GITLAB_GROUP.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(2);
    }
}
