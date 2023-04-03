package com.baiyi.opscloud.factory.gitlab.impl;

import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.factory.gitlab.GitLabEventNameEnum;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/11/1 2:26 下午
 * @Version 1.0
 */
@Component
public class GitLabProjectEventConsumer extends AbstractGitlabEventConsumer {

    private final static GitLabEventNameEnum[] eventNameEnums = {
            GitLabEventNameEnum.PROJECT_CREATE,
            GitLabEventNameEnum.PROJECT_DESTROY,
            GitLabEventNameEnum.PROJECT_RENAME
    };

    @Override
    protected GitLabEventNameEnum[] getEventNameEnums() {
        return eventNameEnums;
    }

    @Override
    protected String getAssetType() {
        return DsAssetTypeConstants.GITLAB_PROJECT.name();
    }

}

