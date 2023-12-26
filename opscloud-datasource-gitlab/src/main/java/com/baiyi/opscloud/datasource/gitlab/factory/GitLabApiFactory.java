package com.baiyi.opscloud.datasource.gitlab.factory;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
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

    public static GitLabApi buildGitLabApi(GitLabConfig.GitLab gitlab) {
        assert gitlab != null;
        final String version = Optional.of(gitlab)
                .map(GitLabConfig.GitLab::getApi)
                .map(GitLabConfig.Api::getVersion)
                .orElse("v4");
        GitLabApi gitLabApi = buildWithVersion(version, gitlab);
        int connectTimeout = Optional.of(gitlab)
                .map(GitLabConfig.GitLab::getApi)
                .map(GitLabConfig.Api::getConnectTimeout)
                .orElse(CONNECT_TIMEOUT);
        int readTimeout = Optional.of(gitlab)
                .map(GitLabConfig.GitLab::getApi)
                .map(GitLabConfig.Api::getReadTimeout)
                .orElse(READ_TIMEOUT);
        gitLabApi.setRequestTimeout(connectTimeout, readTimeout);
        return gitLabApi;
    }

    private static GitLabApi buildWithVersion(String version, GitLabConfig.GitLab gitlab) {
        if (version.equalsIgnoreCase(GitLabApi.ApiVersion.V3.name())) {
            return new GitLabApi(GitLabApi.ApiVersion.V3, gitlab.getUrl(), gitlab.getToken());
        }
        return new GitLabApi(GitLabApi.ApiVersion.V4, gitlab.getUrl(), gitlab.getToken());
    }

}