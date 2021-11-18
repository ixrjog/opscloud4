package com.baiyi.opscloud.datasource.gitlab.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.gitlab.convert.GitlabAssetConvert;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.datasource.gitlab.datasource.GitlabUserDatasource;
import com.google.common.collect.Lists;
import org.gitlab.api.models.GitlabSSHKey;
import org.gitlab.api.models.GitlabUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_GITLAB_USER;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:38 下午
 * @Version 1.0
 */
@Component
public class GitlabUserProvider extends AbstractAssetRelationProvider<GitlabUser, GitlabSSHKey> {

    @Resource
    private GitlabUserProvider gitlabUserProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.GITLAB_USER.getType();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.GITLAB_SSHKEY.getType();
    }

    private GitlabConfig.Gitlab buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, GitlabConfig.class).getGitlab();
    }

    @Override
    protected List<GitlabUser> listEntities(DsInstanceContext dsInstanceContext, GitlabSSHKey target) {
        GitlabConfig.Gitlab gitlab = buildConfig(dsInstanceContext.getDsConfig());
        List<GitlabUser> users = Lists.newArrayList();
        try {
            users.add(GitlabUserDatasource.getUser(gitlab, target.getUser().getId()));
        } catch (IOException ignored) {
        }
        return users;
    }


    @Override
    protected List<GitlabUser> listEntities(DsInstanceContext dsInstanceContext) {
        return GitlabUserDatasource.queryUsers(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = PULL_GITLAB_USER, lockTime = "5m")
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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabUser entity) {
        return GitlabAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(gitlabUserProvider);
    }
}
