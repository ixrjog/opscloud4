package com.baiyi.caesar.gitlab.handler;

import com.baiyi.caesar.common.datasource.config.GitlabDsConfig;
import com.baiyi.caesar.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabUser;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:42 下午
 * @Version 1.0
 */
public class GitlabUserHandler {

    public static List<GitlabUser> queryUsers(GitlabDsConfig.Gitlab gitlab) {
        return buildAPI(gitlab).getUsers();
    }

    private static GitlabAPI buildAPI(GitlabDsConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
