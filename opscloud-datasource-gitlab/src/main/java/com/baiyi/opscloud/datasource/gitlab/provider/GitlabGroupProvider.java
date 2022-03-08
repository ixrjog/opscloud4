package com.baiyi.opscloud.datasource.gitlab.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.gitlab.convert.GitlabAssetConvert;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.datasource.gitlab.driver.GitlabGroupDriver;
import com.google.common.collect.Lists;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_GITLAB_GROUP;

/**
 * @Author baiyi
 * @Date 2021/6/21 6:48 下午
 * @Version 1.0
 */
@Component
public class GitlabGroupProvider extends AbstractAssetRelationProvider<GitlabGroup, GitlabProject> {

    @Resource
    private GitlabGroupProvider gitlabGroupProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.name();
    }

    private GitlabConfig.Gitlab buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, GitlabConfig.class).getGitlab();
    }

    @Override
    protected List<GitlabGroup> listEntities(DsInstanceContext dsInstanceContext, GitlabProject target) {
        return Collections.emptyList();
    }

    @Override
    protected List<GitlabGroup> listEntities(DsInstanceContext dsInstanceContext) {
        try {
            return GitlabGroupDriver.queryGroups(buildConfig(dsInstanceContext.getDsConfig()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Lists.newArrayList();
    }

    @Override
    @SingleTask(name = PULL_GITLAB_GROUP, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.GITLAB_GROUP.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.GITLAB_PROJECT.name();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetKey(), asset.getAssetKey()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabGroup entity) {
        return GitlabAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(gitlabGroupProvider);
    }
}
