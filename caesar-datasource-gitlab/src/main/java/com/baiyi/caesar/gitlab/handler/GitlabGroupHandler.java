package com.baiyi.caesar.gitlab.handler;

import com.baiyi.caesar.common.datasource.config.GitlabDsConfig;
import com.baiyi.caesar.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabGroup;

import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/21 6:32 下午
 * @Version 1.0
 */
public class GitlabGroupHandler {

    public static List<GitlabGroup> queryGroups(GitlabDsConfig.Gitlab gitlab) throws IOException {
        return buildAPI(gitlab).getGroups();
    }

//    public static List<GitlabGroup> queryGroups(GitlabDsConfig.Gitlab gitlab, Integer projectId) throws IOException {
//        GitlabProject gitlabProject = buildAPI(gitlab).getProject(projectId);
//        gitlabProject.
//    }

    private static GitlabAPI buildAPI(GitlabDsConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
