package com.baiyi.opscloud.datasource.gitlab.driver.feature;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitLabApiFactory;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Membership;
import org.gitlab4j.api.models.SshKey;
import org.gitlab4j.api.models.User;

import java.util.List;

/**
 * https://docs.gitlab.com/ee/api/users.html
 * @Author baiyi
 * @Date 2022/10/26 13:32
 * @Version 1.0
 */
public class GitLabUserDriver {


    // private static final int ITEMS_PER_PAGE = 50;

    public static List<User> findUsers(GitlabConfig.Gitlab gitlab, String emailOrUsername) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().findUsers(emailOrUsername);
    }

    public static User getUser(GitlabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().getUser(userId);
    }

    public static List<User> getUsers(GitlabConfig.Gitlab gitlab) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().getUsers();
    }

    public static List<SshKey> getUserSshKeys(GitlabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().getSshKeys(userId);
    }

    public static List<Membership> getUserMemberships(GitlabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().getMemberships(userId);
    }


    /**
     * 锁定用户
     * @param gitlab
     * @param userId
     * @throws GitLabApiException
     */
    public static void blockUser(GitlabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
         buildAPI(gitlab).getUserApi().blockUser(userId);
    }

    /**
     * 解锁用户
     * @param gitlab
     * @param userId
     * @throws GitLabApiException
     */
    public static void unblockUser(GitlabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        buildAPI(gitlab).getUserApi().unblockUser(userId);
    }

    @Deprecated
    public static SshKey getSshKey(GitlabConfig.Gitlab gitlab, Long keyId) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().getSshKey(keyId);
    }

    private static GitLabApi buildAPI(GitlabConfig.Gitlab gitlab) {
        return GitLabApiFactory.buildGitLabApi(gitlab);
    }

}
