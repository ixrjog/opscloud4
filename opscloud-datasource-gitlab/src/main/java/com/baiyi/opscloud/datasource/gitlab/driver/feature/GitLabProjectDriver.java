package com.baiyi.opscloud.datasource.gitlab.driver.feature;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitLabApiFactory;
import org.gitlab4j.api.GitLabApi;

/**
 * @Author baiyi
 * @Date 2022/10/26 15:32
 * @Version 1.0
 */
public class GitLabProjectDriver {

    private static GitLabApi buildAPI(GitlabConfig.Gitlab gitlab) {
        return GitLabApiFactory.buildGitLabApi(gitlab);
    }

}
