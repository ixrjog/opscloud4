package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitLabApiFactory;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.SshKey;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/10/27 15:40
 * @Version 1.0
 */
public class GitLabSshKeyDriver {

    /**
     * 查询用户所有SshKeys
     *
     * @param gitlab
     * @param userId
     * @return
     * @throws GitLabApiException
     */
    public static List<SshKey> getSshKeysWithUserId(GitlabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().getSshKeys(userId);
    }

    private static GitLabApi buildAPI(GitlabConfig.Gitlab gitlab) {
        return GitLabApiFactory.buildGitLabApi(gitlab);
    }

}
