package com.baiyi.opscloud.datasource;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.provider.base.asset.SimpleAssetProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/10 1:50 下午
 * @Version 1.0
 */
@Slf4j
public class AssetTest extends BaseUnit {

    @Resource
    private ApplicationContext applicationContext;

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


    @Test
    void xxxx() {
        Map<String, Object> map =  applicationContext.getBeansWithAnnotation(RestController.class);
        System.err.println(map);
    }
}
