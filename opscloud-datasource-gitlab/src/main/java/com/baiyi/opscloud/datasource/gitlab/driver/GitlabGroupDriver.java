package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabAccessLevel;

import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2021/6/21 6:32 下午
 * @Version 1.0
 */
public class GitlabGroupDriver {

    public static void addGroupMember(GitlabConfig.Gitlab gitlab, Integer groupId, Integer userId, GitlabAccessLevel accessLevel) throws IOException {
        buildAPI(gitlab).addGroupMember(groupId, userId, accessLevel);
    }

    public static void deleteGroupMember(GitlabConfig.Gitlab gitlab, Integer groupId, Integer userId) throws IOException {
        buildAPI(gitlab).deleteGroupMember(groupId, userId);
    }

    private static GitlabAPI buildAPI(GitlabConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
