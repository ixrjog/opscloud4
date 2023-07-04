package com.baiyi.opscloud.factory.gitlab.impl;

import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.factory.gitlab.enums.GitLabEventNameEnum;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/11/1 2:26 下午
 * @Version 1.0
 */
@Component
public class GitLabProjectEventConsumer extends AbstractGitLabEventConsumer {

    private final static GitLabEventNameEnum[] EVENT_NAME_ENUMS = {
            GitLabEventNameEnum.PROJECT_CREATE,
            GitLabEventNameEnum.PROJECT_DESTROY,
            GitLabEventNameEnum.PROJECT_RENAME
    };

    @Override
    protected GitLabEventNameEnum[] getEventNameEnums() {
        return EVENT_NAME_ENUMS;
    }

    @Override
    protected String getAssetType() {
        return DsAssetTypeConstants.GITLAB_PROJECT.name();
    }

}

