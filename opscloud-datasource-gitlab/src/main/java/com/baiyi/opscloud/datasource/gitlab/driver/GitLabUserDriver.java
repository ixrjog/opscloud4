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
        try (GitLabApi gitLabApi = buildAPI(gitlab)) {
            return gitLabApi.getUserApi().findUsers(emailOrUsername);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
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
        try (GitLabApi gitLabApi = buildAPI(gitlab)) {
            return gitLabApi.getUserApi().getUser(userId);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 查询GitLab实例中所有用户
     *
     * @param gitlab
     * @return
     * @throws GitLabApiException
     */
    public static List<User> getUsers(GitLabConfig.Gitlab gitlab) throws GitLabApiException {
        try (GitLabApi gitLabApi = buildAPI(gitlab)) {
            return gitLabApi.getUserApi().getUsers();
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }


    public static List<Membership> getUserMemberships(GitLabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        try (GitLabApi gitLabApi = buildAPI(gitlab)) {
            return gitLabApi.getUserApi().getMemberships(userId);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 设置用户头像
     *
     * @param gitlab
     * @param userId
     * @param avatarFile
     * @throws GitLabApiException
     */
    public static void updateUser(GitLabConfig.Gitlab gitlab, Long userId, File avatarFile) throws GitLabApiException {
        try (GitLabApi gitLabApi = buildAPI(gitlab)) {
            gitLabApi.getUserApi().setUserAvatar(userId, avatarFile);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 锁定用户
     *
     * @param gitlab
     * @param userId
     * @throws GitLabApiException
     */
    public static void blockUser(GitLabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        try (GitLabApi gitLabApi = buildAPI(gitlab)) {
            gitLabApi.getUserApi().blockUser(userId);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 解锁用户
     *
     * @param gitlab
     * @param userId
     * @throws GitLabApiException
     */
    public static void unblockUser(GitLabConfig.Gitlab gitlab, Long userId) throws GitLabApiException {
        try (GitLabApi gitLabApi = buildAPI(gitlab)) {
            gitLabApi.getUserApi().unblockUser(userId);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public static User createUser(GitLabConfig.Gitlab gitlab, User user, String password) throws GitLabApiException {
        try (GitLabApi gitLabApi = buildAPI(gitlab)) {
            return gitLabApi.getUserApi().createUser(user, password, false);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private static GitLabApi buildAPI(GitLabConfig.Gitlab gitlab) {
        return GitLabApiFactory.buildGitLabApi(gitlab);
    }

}
