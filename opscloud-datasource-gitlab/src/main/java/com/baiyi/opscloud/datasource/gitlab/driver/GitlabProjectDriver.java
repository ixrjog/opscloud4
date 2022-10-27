package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabProjectMember;

import java.io.IOException;
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

    /**
     * 查询项目成员 最大查询20
     * @param gitlab
     * @param projectId
     * @return
     * @throws IOException
     */
    public static List<GitlabProjectMember> getProjectMembers(GitlabConfig.Gitlab gitlab, Integer projectId) throws IOException {
       return buildAPI(gitlab).getProjectMembers(projectId);
    }


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

    public static List<GitlabProject> queryGroupProjects(GitlabConfig.Gitlab gitlab, Integer groupId) {
        return buildAPI(gitlab).getGroupProjects(groupId);
    }

    private static GitlabAPI buildAPI(GitlabConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
