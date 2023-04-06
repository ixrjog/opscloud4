package com.baiyi.opscloud.datasource;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;
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

    @Resource
    private DsInstanceAssetFacade assetFacade;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Test
    void pullAsset() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.LDAP.getName(), DsAssetTypeConstants.USER.name());
        assert assetProvider != null;
        assetProvider.pullAsset(1);
    }

    @Test
    void pullGroup() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.LDAP.getName(), DsAssetTypeConstants.GROUP.name());
        assert assetProvider != null;
        assetProvider.pullAsset(1);
    }

    @Test
    void pullAssetByGitlabUser() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.GITLAB.getName(), DsAssetTypeConstants.USER.name());
        assert assetProvider != null;
        assetProvider.pullAsset(2);
    }

    @Test
    void pullAssetByGitlabProject() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.GITLAB.getName(), DsAssetTypeConstants.GITLAB_PROJECT.name());
        assert assetProvider != null;
        assetProvider.pullAsset(2);
    }

    @Test
    void pullAssetByGitlabGroup() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.GITLAB.getName(), DsAssetTypeConstants.GITLAB_GROUP.name());
        assert assetProvider != null;
        assetProvider.pullAsset(2);
    }


    @Test
    void xxxx() {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(RestController.class);
        System.err.println(map);
    }

    @Test
    void deleteAsset() {
        String chuangyi = "e9f2acfe1d2945dd91262ba49df26984";
        String pp = "aea8b524d19042af97e284da75d1eaba";

        String DINGTALK_USER = "DINGTALK_USER";
        String DINGTALK_DEPARTMENT = "DINGTALK_DEPARTMENT";
        List<DatasourceInstanceAsset> assetList =
                dsInstanceAssetService.listByInstanceAssetType(pp, DINGTALK_USER);
        assetList.forEach( asset -> assetFacade.deleteAssetByAssetId(asset.getId()));

    }

}
