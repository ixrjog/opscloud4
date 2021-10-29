package com.baiyi.opscloud.factory.gitlab.impl;

import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.factory.gitlab.GitlabEventNameEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/10/29 11:03 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class GitlabKeyEventConsume extends AbstractGitlabEventConsume {

    private final static GitlabEventNameEnum[] eventNameEnums = {
            GitlabEventNameEnum.KEY_CREATE,
            GitlabEventNameEnum.KEY_DESTROY};

    @Override
    protected GitlabEventNameEnum[] getEventNameEnums() {
        return eventNameEnums;
    }

    @Override
    protected  String getAssetType(){
        return DsAssetTypeEnum.GITLAB_SSHKEY.name();
    }

}
