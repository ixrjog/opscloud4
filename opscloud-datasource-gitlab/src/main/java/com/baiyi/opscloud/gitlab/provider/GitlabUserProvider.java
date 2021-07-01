package com.baiyi.opscloud.gitlab.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.GitlabDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsGitlabConfig;
import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.builder.AssetContainer;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.util.AssetUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.gitlab.convert.GitlabAssetConvert;
import com.baiyi.opscloud.gitlab.handler.GitlabUserHandler;
import org.gitlab.api.models.GitlabUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:38 下午
 * @Version 1.0
 */
@Component
public class GitlabUserProvider extends BaseAssetProvider<GitlabUser> {

    @Resource
    private GitlabUserProvider gitlabUserProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.USER.getType();
    }

    private DsGitlabConfig.Gitlab buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, GitlabDsInstanceConfig.class).getGitlab();
    }

    @Override
    protected List<GitlabUser> listEntries(DsInstanceContext dsInstanceContext) {
        return GitlabUserHandler.queryUsers(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = "PullGitlabUser", lockTime = 300)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (preAsset.getIsActive() != asset.getIsActive())
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabUser entry) {
        return GitlabAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(gitlabUserProvider);
    }
}
