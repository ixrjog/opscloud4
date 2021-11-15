package com.baiyi.opscloud.datasource.gitlab.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.GitlabDsInstanceConfig;
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
import com.baiyi.opscloud.datasource.gitlab.handler.GitlabProjectHandler;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/21 6:40 下午
 * @Version 1.0
 */
@Component
public class GitlabProjectProvider extends AbstractAssetRelationProvider<GitlabProject, GitlabGroup> {

    @Resource
    private GitlabProjectProvider gitlabProjectProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.name();
    }

    private GitlabDsInstanceConfig.Gitlab buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, GitlabDsInstanceConfig.class).getGitlab();
    }

    @Override
    protected List<GitlabProject> listEntries(DsInstanceContext dsInstanceContext, GitlabGroup target) {
        return GitlabProjectHandler.queryGroupProjects(buildConfig(dsInstanceContext.getDsConfig()), target.getId());
    }

    @Override
    protected List<GitlabProject> listEntries(DsInstanceContext dsInstanceContext) {
        return GitlabProjectHandler.queryProjects(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = "PullGitlabProject", lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.GITLAB_PROJECT.getType();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.GITLAB_GROUP.getType();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetKey(), asset.getAssetKey()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabProject entry) {
        return GitlabAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(gitlabProjectProvider);
    }
}
