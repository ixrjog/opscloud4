package com.baiyi.opscloud.factory.gitlab.impl;

import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.factory.gitlab.GitlabEventNameEnum;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/11/1 2:26 下午
 * @Version 1.0
 */
@Component
public class GitlabProjectEventConsume extends AbstractGitlabEventConsume {

    private final static GitlabEventNameEnum[] eventNameEnums = {
            GitlabEventNameEnum.PROJECT_CREATE,
            GitlabEventNameEnum.PROJECT_DESTROY,
            GitlabEventNameEnum.PROJECT_RENAME
    };

    @Override
    protected GitlabEventNameEnum[] getEventNameEnums() {
        return eventNameEnums;
    }

    @Override
    protected String getAssetType() {
        return DsAssetTypeEnum.GITLAB_PROJECT.name();
    }

}

