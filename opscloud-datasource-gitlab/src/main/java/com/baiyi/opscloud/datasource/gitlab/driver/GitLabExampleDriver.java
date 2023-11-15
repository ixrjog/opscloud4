package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.client.GitLabApiBuilder;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.CompareResults;

/**
 * @Author baiyi
 * @Date 2023/11/14 16:23
 * @Version 1.0
 */
@Slf4j
public class GitLabExampleDriver {

    public static CompareResults compare(GitLabConfig.GitLab gitlab, Long projectId, String from, String to) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getRepositoryApi().compare(projectId, from, to);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}
