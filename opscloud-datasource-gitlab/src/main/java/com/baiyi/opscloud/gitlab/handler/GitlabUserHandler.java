package com.baiyi.opscloud.gitlab.handler;

import com.baiyi.opscloud.common.datasource.GitlabDsInstanceConfig;
import com.baiyi.opscloud.gitlab.factory.GitlabFactory;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabSSHKey;
import org.gitlab.api.models.GitlabUser;

import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:42 下午
 * @Version 1.0
 */
public class GitlabUserHandler {

    public static GitlabUser getUser(GitlabDsInstanceConfig.Gitlab gitlab, Integer userId) throws IOException {
        return buildAPI(gitlab).getUser(userId);
    }

    public static List<GitlabUser> queryUsers(GitlabDsInstanceConfig.Gitlab gitlab) {
        return buildAPI(gitlab).getUsers();
    }

    public static List<GitlabSSHKey> getUserSSHKeys(GitlabDsInstanceConfig.Gitlab gitlab, Integer targetUserId) throws IOException {
        return buildAPI(gitlab).getSSHKeys(targetUserId);
    }

    public static GitlabSSHKey getSSHKeys(GitlabDsInstanceConfig.Gitlab gitlab, Integer keyId) throws IOException {
        return buildAPI(gitlab).getSSHKey(keyId);
    }

    private static GitlabAPI buildAPI(GitlabDsInstanceConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
