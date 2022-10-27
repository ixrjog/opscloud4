package com.baiyi.opscloud.datasource.gitlab.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.gitlab.convert.GitlabAssetConvert;
import com.baiyi.opscloud.datasource.gitlab.driver.feature.GitLabGroupDriver;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabProjectDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Project;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_GITLAB_PROJECT;

/**
 * @Author baiyi
 * @Date 2021/6/21 6:40 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class GitLabProjectProvider extends AbstractAssetRelationProvider<Project, Group> {

    @Resource
    private GitLabProjectProvider gitLabProjectProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.name();
    }

    private GitlabConfig.Gitlab buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, GitlabConfig.class).getGitlab();
    }

    @Override
    protected List<Project> listEntities(DsInstanceContext dsInstanceContext, Group target) {
        try {
            return GitLabGroupDriver.getProjectsWithGroupId(buildConfig(dsInstanceContext.getDsConfig()), target.getId());
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    protected List<Project> listEntities(DsInstanceContext dsInstanceContext) {
        try {
            return GitLabProjectDriver.getProjects(buildConfig(dsInstanceContext.getDsConfig()));
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    @SingleTask(name = PULL_GITLAB_PROJECT, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.GITLAB_PROJECT.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.GITLAB_GROUP.name();
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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Project entity) {
        return GitlabAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(gitLabProjectProvider);
    }
}
