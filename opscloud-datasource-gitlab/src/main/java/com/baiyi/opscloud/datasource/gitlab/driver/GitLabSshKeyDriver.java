package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.client.GitLabApiBuilder;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.SshKey;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/10/27 15:40
 * @Version 1.0
 */
@Slf4j
public class GitLabSshKeyDriver {

    /**
     * 查询用户所有SshKey
     *
     * @param gitlab
     * @param userId
     * @return
     * @throws GitLabApiException
     */
    public static List<SshKey> getSshKeysWithUserId(GitLabConfig.GitLab gitlab, Long userId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getUserApi().getSshKeys(userId);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}