package com.baiyi.opscloud.datasource.gitlab.handler;

import com.baiyi.opscloud.common.datasource.GitlabDsInstanceConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/21 6:29 下午
 * @Version 1.0
 */
public class GitlabProjectHandler {

    public static List<GitlabProject> queryProjects(GitlabDsInstanceConfig.Gitlab gitlab) {
        return buildAPI(gitlab).getProjects();
    }

    public static List<GitlabProject> queryGroupProjects(GitlabDsInstanceConfig.Gitlab gitlab, Integer groupId) {
        return buildAPI(gitlab).getGroupProjects(groupId);
    }

    private static GitlabAPI buildAPI(GitlabDsInstanceConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
