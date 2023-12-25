package com.baiyi.opscloud.datasource.gitlab.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.gitlab.converter.GitLabAssetConverter;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabGroupDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Project;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_GITLAB_GROUP;

/**
 * @Author baiyi
 * @Date 2021/6/21 6:48 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class GitLabGroupProvider extends AbstractAssetRelationProvider<Group, Project> {

    @Resource
    private GitLabGroupProvider gitLabGroupProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.name();
    }

    private GitLabConfig.GitLab buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, GitLabConfig.class).getGitlab();
    }

    @Override
    protected List<Group> listEntities(DsInstanceContext dsInstanceContext, Project target) {
        return Collections.emptyList();
    }

    @Override
    protected List<Group> listEntities(DsInstanceContext dsInstanceContext) {
        try {
            return GitLabGroupDriver.getGroups(buildConfig(dsInstanceContext.getDsConfig()));
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
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
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKey()
                .compareOfKey2()
                .compareOfDescription()
                .build();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Group entity) {
        return GitLabAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(gitLabGroupProvider);
    }

}