package com.baiyi.opscloud.datasource.gitlab.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.gitlab.convert.GitlabAssetConvert;
import com.baiyi.opscloud.datasource.gitlab.driver.feature.GitLabSshKeyDriver;
import com.baiyi.opscloud.datasource.gitlab.driver.feature.GitLabUserDriver;
import com.baiyi.opscloud.datasource.gitlab.entity.SshKeyBO;
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
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_GITLAB_SSH_KEY;

/**
 * @Author baiyi
 * @Date 2021/7/2 3:01 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class GitlabSSHKey2Provider extends AbstractAssetRelationProvider<SshKey, User> {

    @Resource
    private GitlabSSHKey2Provider gitlabSSHKeyProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.name();
    }

    private GitlabConfig.Gitlab buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, GitlabConfig.class).getGitlab();
    }

    @Override
    protected List<SshKey> listEntities(DsInstanceContext dsInstanceContext, User target) {
        GitlabConfig.Gitlab gitlab = buildConfig(dsInstanceContext.getDsConfig());
        try {
            return GitLabSshKeyDriver.getSshKeysWithUserId(gitlab, target.getId()).stream().map(e -> {
                        SshKeyBO sshKey = BeanCopierUtil.copyProperties(e, SshKeyBO.class);
                        sshKey.setUsername(target.getUsername());
                        return sshKey;
                    }
            ).collect(Collectors.toList());
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    protected List<SshKey> listEntities(DsInstanceContext dsInstanceContext) {
        GitlabConfig.Gitlab gitlab = buildConfig(dsInstanceContext.getDsConfig());
        try {

            List<User> users = GitLabUserDriver.getUsers(gitlab);
            if (CollectionUtils.isEmpty(users))
                return Collections.emptyList();
            List<SshKey> sshKeys = Lists.newArrayList();
            for (User user : users) {
                List<SshKey> keys = GitLabSshKeyDriver.getSshKeysWithUserId(gitlab, user.getId());
                if (!CollectionUtils.isEmpty(keys)) {
                    sshKeys.addAll(keys.stream().map(e -> {
                                SshKeyBO sshKey = BeanCopierUtil.copyProperties(e, SshKeyBO.class);
                                sshKey.setUsername(user.getUsername());
                                return sshKey;
                            }).collect(Collectors.toList())
                    );
                }
            }
            return  sshKeys;
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, SshKey entity) {
        return GitlabAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(gitlabSSHKeyProvider);
    }
}
