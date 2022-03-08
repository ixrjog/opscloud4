package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitlabFactory;
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
public class GitlabUserDriver {

    public static GitlabUser getUser(GitlabConfig.Gitlab gitlab, Integer userId) throws IOException {
        return buildAPI(gitlab).getUser(userId);
    }

    public static List<GitlabUser> queryUsers(GitlabConfig.Gitlab gitlab) {
        return buildAPI(gitlab).getUsers();
    }

    public static List<GitlabSSHKey> getUserSSHKeys(GitlabConfig.Gitlab gitlab, Integer targetUserId) throws IOException {
        return buildAPI(gitlab).getSSHKeys(targetUserId);
    }

    public static GitlabSSHKey getSSHKeys(GitlabConfig.Gitlab gitlab, Integer keyId) throws IOException {
        return buildAPI(gitlab).getSSHKey(keyId);
    }

    private static GitlabAPI buildAPI(GitlabConfig.Gitlab gitlab) {
        return GitlabFactory.buildGitlabAPI(gitlab);
    }
}
