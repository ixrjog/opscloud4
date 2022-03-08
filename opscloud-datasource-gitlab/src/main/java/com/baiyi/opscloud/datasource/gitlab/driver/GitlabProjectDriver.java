package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/21 6:29 下午
 * @Version 1.0
 */
public class GitlabProjectDriver {

    public static List<GitlabProject> queryProjects(GitlabConfig.Gitlab gitlab) {
        return buildAPI(gitlab).getProjects();
    }

    public static List<GitlabProject> queryGroupProjects(GitlabConfig.Gitlab gitlab, Integer groupId) {
        return buildAPI(gitlab).getGroupProjects(groupId);
    }

    private static GitlabAPI buildAPI(GitlabConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
