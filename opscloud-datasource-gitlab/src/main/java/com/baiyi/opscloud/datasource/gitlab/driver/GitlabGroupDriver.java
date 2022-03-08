package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabGroup;

import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/21 6:32 下午
 * @Version 1.0
 */
public class GitlabGroupDriver {

    public static List<GitlabGroup> queryGroups(GitlabConfig.Gitlab gitlab) throws IOException {
        return buildAPI(gitlab).getGroups();
    }

//    public static List<GitlabGroup> queryGroups(GitlabDsConfig.Gitlab gitlab, Integer projectId) throws IOException {
//        GitlabProject gitlabProject = buildAPI(gitlab).getProject(projectId);
//        gitlabProject.
//    }

    private static GitlabAPI buildAPI(GitlabConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
