package com.baiyi.opscloud.datasource.gitlab.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.gitlab.convert.GitlabAssetConvert;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabUserDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.SshKey;
import org.gitlab4j.api.models.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_GITLAB_USER;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:38 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class GitLabUserProvider extends AbstractAssetRelationProvider<User, SshKey> {

    @Resource
    private GitLabUserProvider gitLabUserProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.GITLAB_USER.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.GITLAB_SSHKEY.name();
    }

    private GitLabConfig.Gitlab buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, GitLabConfig.class).getGitlab();
    }

    @Override
    protected List<User> listEntities(DsInstanceContext dsInstanceContext, SshKey target) {
        GitLabConfig.Gitlab gitlab = buildConfig(dsInstanceContext.getDsConfig());
        try {
            return Lists.newArrayList(GitLabUserDriver.getUser(gitlab, target.getUserId()));
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    protected List<User> listEntities(DsInstanceContext dsInstanceContext) {
        try {
            return GitLabUserDriver.getUsers(buildConfig(dsInstanceContext.getDsConfig()));
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    @SingleTask(name = PULL_GITLAB_USER, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2())) return false;
        if (!AssetUtil.equals(preAsset.getName(), asset.getName())) return false;
        if (preAsset.getIsActive() != asset.getIsActive()) return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, User entity) {
        return GitlabAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(gitLabUserProvider);
    }
}
