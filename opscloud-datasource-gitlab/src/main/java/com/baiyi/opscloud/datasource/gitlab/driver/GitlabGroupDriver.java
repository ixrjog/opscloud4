package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabGroupMember;

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

    public static void addGroupMember(GitlabConfig.Gitlab gitlab, Integer groupId, Integer userId, GitlabAccessLevel accessLevel) throws IOException {
        buildAPI(gitlab).addGroupMember(groupId, userId, accessLevel);
    }

    public static void deleteGroupMember(GitlabConfig.Gitlab gitlab, Integer groupId, Integer userId) throws IOException {
        buildAPI(gitlab).deleteGroupMember(groupId, userId);
    }

    public static List<GitlabGroupMember> getGroupMembers(GitlabConfig.Gitlab gitlab, Integer groupId) {
        return buildAPI(gitlab).getGroupMembers(groupId);
    }

    private static GitlabAPI buildAPI(GitlabConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
