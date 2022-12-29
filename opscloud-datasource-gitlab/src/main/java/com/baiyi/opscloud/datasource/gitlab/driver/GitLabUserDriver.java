package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitLabApiFactory;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Membership;
import org.gitlab4j.api.models.User;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * https://docs.gitlab.com/ee/api/users.html
 *
 * @Author baiyi
 * @Date 2022/10/26 13:32
 * @Version 1.0
 */
@Slf4j
@Component
public class GitLabUserDriver {


    // private static final int ITEMS_PER_PAGE = 50;

    /**
     * 按email或username查询用户
     *
     * @param gitlab
     * @param emailOrUsername
     * @return
     * @throws GitLabApiException
     */
    public static List<User> findUsers(GitLabConfig.Gitlab gitlab, String emailOrUsername) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().findUsers(emailOrUsername);
    }

    /**
     * 查询用户
     *
     * @param gitlab
     * @param userId
     * @return
     * @throws GitLabApiException
     */
    public static User getUser(GitLabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().getUser(userId);
    }

    /**
     * 查询GitLab实例中所有用户
     *
     * @param gitlab
     * @return
     * @throws GitLabApiException
     */
    public static List<User> getUsers(GitLabConfig.Gitlab gitlab) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().getUsers();
    }


    public static List<Membership> getUserMemberships(GitLabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().getMemberships(userId);
    }

    /**
     * 设置用户头像
     * @param gitlab
     * @param userId
     * @param avatarFile
     * @throws GitLabApiException
     */
    public static void updateUser(GitLabConfig.Gitlab gitlab, Long userId, File avatarFile) throws GitLabApiException {
        buildAPI(gitlab).getUserApi().setUserAvatar(userId, avatarFile);
    }

    /**
     * 锁定用户
     *
     * @param gitlab
     * @param userId
     * @throws GitLabApiException
     */
    public static void blockUser(GitLabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        buildAPI(gitlab).getUserApi().blockUser(userId);
    }

    /**
     * 解锁用户
     *
     * @param gitlab
     * @param userId
     * @throws GitLabApiException
     */
    public static void unblockUser(GitLabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        buildAPI(gitlab).getUserApi().unblockUser(userId);
    }

    public static User createUser(GitLabConfig.Gitlab gitlab, User user, String password) throws GitLabApiException {
        return buildAPI(gitlab).getUserApi().createUser(user, password, false);
    }

    private static GitLabApi buildAPI(GitLabConfig.Gitlab gitlab) {
        return GitLabApiFactory.buildGitLabApi(gitlab);
    }

}
