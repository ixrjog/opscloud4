package com.baiyi.opscloud.datasource.gitlab.factory;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import org.gitlab4j.api.GitLabApi;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/10/26 11:24
 * @Version 1.0
 */
public class GitLabApiFactory {

    // Set the connect timeout to 1 second and the read timeout to 5 seconds
    private static final int CONNECT_TIMEOUT = 1000;
    private static final int READ_TIMEOUT = 5000;

    public static GitLabApi buildGitLabApi(GitlabConfig.Gitlab gitlab) {
        assert gitlab != null;
        String version = Optional.of(gitlab)
                .map(GitlabConfig.Gitlab::getApi)
                .map(GitlabConfig.Api::getVersion)
                .orElse("v4");
        GitLabApi gitLabApi;
        if (version.equalsIgnoreCase("v3")) {
            gitLabApi = new GitLabApi(GitLabApi.ApiVersion.V3, gitlab.getUrl(), gitlab.getToken());
        } else {
            gitLabApi = new GitLabApi(GitLabApi.ApiVersion.V4, gitlab.getUrl(), gitlab.getToken());
        }

        int connectTimeout = Optional.of(gitlab)
                .map(GitlabConfig.Gitlab::getApi)
                .map(GitlabConfig.Api::getConnectTimeout)
                .orElse(CONNECT_TIMEOUT);
        int readTimeout = Optional.of(gitlab)
                .map(GitlabConfig.Gitlab::getApi)
                .map(GitlabConfig.Api::getReadTimeout)
                .orElse(READ_TIMEOUT);
        gitLabApi.setRequestTimeout(connectTimeout, readTimeout);
        return gitLabApi;
    }

}
