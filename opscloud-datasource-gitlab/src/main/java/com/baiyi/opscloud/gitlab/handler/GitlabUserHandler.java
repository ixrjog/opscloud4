package com.baiyi.opscloud.gitlab.handler;

import com.baiyi.opscloud.common.datasource.config.DsGitlabConfig;
import com.baiyi.opscloud.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabUser;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:42 下午
 * @Version 1.0
 */
public class GitlabUserHandler {

    public static List<GitlabUser> queryUsers(DsGitlabConfig.Gitlab gitlab) {
        return buildAPI(gitlab).getUsers();
    }

    private static GitlabAPI buildAPI(DsGitlabConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
