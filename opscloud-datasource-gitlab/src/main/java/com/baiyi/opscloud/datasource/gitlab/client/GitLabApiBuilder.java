package com.baiyi.opscloud.datasource.gitlab.client;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitLabApiFactory;
import org.gitlab4j.api.GitLabApi;

/**
 * @Author baiyi
 * @Date 2023/4/24 09:49
 * @Version 1.0
 */
public class GitLabApiBuilder {

    private GitLabApiBuilder() {
    }

    public static GitLabApi build(GitLabConfig.GitLab gitlab) {
        return GitLabApiFactory.buildGitLabApi(gitlab);
    }

}