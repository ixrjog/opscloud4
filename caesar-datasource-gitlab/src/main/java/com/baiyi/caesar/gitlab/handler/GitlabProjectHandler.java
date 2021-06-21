package com.baiyi.caesar.gitlab.handler;

import com.baiyi.caesar.common.datasource.config.GitlabDsConfig;
import com.baiyi.caesar.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/21 6:29 下午
 * @Version 1.0
 */
public class GitlabProjectHandler {

    public static List<GitlabProject> queryProjects(GitlabDsConfig.Gitlab gitlab) {
        return buildAPI(gitlab).getProjects();
    }

    public static List<GitlabProject> queryGroupProjects(GitlabDsConfig.Gitlab gitlab, Integer groupId) {
        return buildAPI(gitlab).getGroupProjects(groupId);
    }

    private static GitlabAPI buildAPI(GitlabDsConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
