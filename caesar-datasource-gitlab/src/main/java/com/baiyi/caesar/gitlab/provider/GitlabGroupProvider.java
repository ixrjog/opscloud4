package com.baiyi.caesar.gitlab.provider;

import com.baiyi.caesar.common.annotation.SingleTask;
import com.baiyi.caesar.common.datasource.GitlabDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.GitlabDsConfig;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.asset.AbstractAssetRelationProvider;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.datasource.util.AssetUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.gitlab.convert.GitlabAssetConvert;
import com.baiyi.caesar.gitlab.handler.GitlabGroupHandler;
import com.google.common.collect.Lists;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

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

    private GitlabDsConfig.Gitlab buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, GitlabDsInstanceConfig.class).getGitlab();
    }

    @Override
    protected List<GitlabGroup> listEntries(DatasourceConfig dsConfig, GitlabProject target) {
        return Lists.newArrayList();
    }

    @Override
    protected List<GitlabGroup> listEntries(DatasourceConfig dsConfig) {
        try{
            GitlabGroupHandler.queryGroups(buildConfig(dsConfig));
        }catch (IOException ioEx){
        }
        return Lists.newArrayList();
    }

    @Override
    @SingleTask(name = "PullGitlabGroup", lockTime = 300)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.GITLAB_GROUP.getType();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.GITLAB_PROJECT.getType();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabGroup entry) {
        return GitlabAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(gitlabGroupProvider);
    }
}
