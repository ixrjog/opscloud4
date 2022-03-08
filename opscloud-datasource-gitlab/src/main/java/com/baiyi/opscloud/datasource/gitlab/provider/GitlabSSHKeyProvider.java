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
import com.baiyi.opscloud.datasource.gitlab.driver.GitlabUserDriver;
import com.google.common.collect.Lists;
import org.gitlab.api.models.GitlabSSHKey;
import org.gitlab.api.models.GitlabUser;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_GITLAB_SSH_KEY;

/**
 * @Author baiyi
 * @Date 2021/7/2 3:01 下午
 * @Version 1.0
 */
@Component
public class GitlabSSHKeyProvider extends AbstractAssetRelationProvider<GitlabSSHKey, GitlabUser> {

    @Resource
    private GitlabSSHKeyProvider gitlabSSHKeyProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.name();
    }

    private GitlabConfig.Gitlab buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, GitlabConfig.class).getGitlab();
    }

    @Override
    protected List<GitlabSSHKey> listEntities(DsInstanceContext dsInstanceContext, GitlabUser target) {
        GitlabConfig.Gitlab gitlab = buildConfig(dsInstanceContext.getDsConfig());
        try {
            return GitlabUserDriver.getUserSSHKeys(gitlab, target.getId()).stream().peek(e ->
                    e.setUser(target)
            ).collect(Collectors.toList());
        } catch (IOException ignored) {
        }
        return Lists.newArrayList();
    }

    @Override
    protected List<GitlabSSHKey> listEntities(DsInstanceContext dsInstanceContext) {
        GitlabConfig.Gitlab gitlab = buildConfig(dsInstanceContext.getDsConfig());
        List<GitlabUser> users = GitlabUserDriver.queryUsers(gitlab);
        List<GitlabSSHKey> keys = Lists.newArrayList();
        if (CollectionUtils.isEmpty(users))
            return keys;
        users.forEach(u -> {
            try {
                keys.addAll(GitlabUserDriver.getUserSSHKeys(gitlab, u.getId()).stream().peek(e ->
                        e.setUser(u)
                ).collect(Collectors.toList()));
            } catch (IOException ignored) {
            }
        });
        return keys;
    }

    @Override
    @SingleTask(name = PULL_GITLAB_SSH_KEY)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.GITLAB_SSHKEY.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.GITLAB_USER.name();
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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabSSHKey entity) {
        return GitlabAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(gitlabSSHKeyProvider);
    }
}

