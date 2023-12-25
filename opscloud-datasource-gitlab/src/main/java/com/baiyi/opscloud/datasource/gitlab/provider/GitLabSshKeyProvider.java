package com.baiyi.opscloud.datasource.gitlab.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.gitlab.converter.GitLabAssetConverter;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabSshKeyDriver;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabUserDriver;
import com.baiyi.opscloud.datasource.gitlab.entity.GitLabSshKey;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.SshKey;
import org.gitlab4j.api.models.User;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
public class GitLabSshKeyProvider extends AbstractAssetRelationProvider<SshKey, User> {

    @Resource
    private GitLabSshKeyProvider gitLabSshKeyProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.name();
    }

    private GitLabConfig.GitLab buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, GitLabConfig.class).getGitlab();
    }

    @Override
    protected List<SshKey> listEntities(DsInstanceContext dsInstanceContext, User target) {
        GitLabConfig.GitLab gitlab = buildConfig(dsInstanceContext.getDsConfig());
        try {
            return GitLabSshKeyDriver.getSshKeysWithUserId(gitlab, target.getId()).stream().map(e -> {
                        GitLabSshKey sshKey = BeanCopierUtil.copyProperties(e, GitLabSshKey.class);
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
        GitLabConfig.GitLab gitlab = buildConfig(dsInstanceContext.getDsConfig());
        try {

            List<User> users = GitLabUserDriver.getUsers(gitlab);
            if (CollectionUtils.isEmpty(users)) {
                return Collections.emptyList();
            }
            List<SshKey> sshKeys = Lists.newArrayList();
            for (User user : users) {
                List<SshKey> keys = GitLabSshKeyDriver.getSshKeysWithUserId(gitlab, user.getId());
                if (!CollectionUtils.isEmpty(keys)) {
                    sshKeys.addAll(keys.stream().map(e -> {
                                GitLabSshKey sshKey = BeanCopierUtil.copyProperties(e, GitLabSshKey.class);
                                sshKey.setUsername(user.getUsername());
                                return sshKey;
                            }).toList()
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
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKey()
                .compareOfKey2()
                .compareOfDescription()
                .build();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, SshKey entity) {
        return GitLabAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(gitLabSshKeyProvider);
    }

}