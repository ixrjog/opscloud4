package com.baiyi.opscloud.datasource.business.account.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.business.account.impl.base.AbstractAccountHandler;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabUserDriver;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/10/28 15:50
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GitLabAccountHandler extends AbstractAccountHandler {

    protected static ThreadLocal<GitLabConfig.GitLab> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigManager.build(dsConfig, GitLabConfig.class).getGitlab());
    }

    @Override
    protected void doCreate(User user) {
        // 不处理
    }

    @Override
    protected void doUpdate(User user) {
        // 不处理
    }

    @Override
    protected void doDelete(User user) {
        try {
            Optional<org.gitlab4j.api.models.User> optionalUser = GitLabUserDriver.findUsers(configContext.get(), user.getUsername())
                    .stream()
                    .filter(e -> e.getUsername().equals(user.getUsername()))
                    .findFirst();
            if (optionalUser.isPresent()) {
                GitLabUserDriver.blockUser(configContext.get(), optionalUser.get().getId());
            }
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void doGrant(User user, BaseBusiness.IBusiness businessResource) {
    }

    @Override
    public void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
    }

    @Override
    protected int getBusinessResourceType() {
        return BusinessTypeEnum.COMMON.getType();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.GITLAB.getName();
    }

}

