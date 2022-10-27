package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabAccessLevel;

import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2021/6/21 6:29 下午
 * @Version 1.0
 */
public class GitlabProjectDriver {

    /**
     * 更新项目成员
     * @param gitlab
     * @param projectId
     * @param userId
     * @param accessLevel
     * @throws IOException
     */
    public static void updateProjectMember(GitlabConfig.Gitlab gitlab, Integer projectId, Integer userId, GitlabAccessLevel accessLevel) throws IOException {
        buildAPI(gitlab).updateProjectMember(projectId, userId, accessLevel);
    }

    /**
     * 新增项目成员
     * @param gitlab
     * @param projectId
     * @param userId
     * @param accessLevel
     * @throws IOException
     */
    public static void addProjectMember(GitlabConfig.Gitlab gitlab, Integer projectId, Integer userId, GitlabAccessLevel accessLevel) throws IOException {
        buildAPI(gitlab).addProjectMember(projectId, userId, accessLevel);
    }

    private static GitlabAPI buildAPI(GitlabConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
