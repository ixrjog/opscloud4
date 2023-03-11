package com.baiyi.opscloud.datasource.gitlab;

import com.baiyi.opscloud.datasource.gitlab.base.BaseGitLabApiUnit;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabProjectDriver;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabSshKeyDriver;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabUserDriver;
import com.baiyi.opscloud.workorder.delegate.GitLabUserDelegate;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/10/26 13:57
 * @Version 1.0
 */
public class GitLabApiTest extends BaseGitLabApiUnit {

    private static final Long BAIYI_USER_ID = 2L;

    private static final Long KEY_ID = 10L;

    @Resource
    private GitLabUserDelegate gitlabUserDelegate;

    /**
     * 模糊查询用户
     */
    @Test
    void findUserTest() {
        try {
            List<User> users = GitLabUserDriver.findUsers(getConfig().getGitlab(), "baiyi");
            print(users.get(0));
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询用户
     */
    @Test
    void getUserTest() {
        try {
            // baiyi  userId = 2L
            User user = GitLabUserDriver.getUser(getConfig().getGitlab(), BAIYI_USER_ID);
            print(user);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getUsersTest() {
        try {
            List<User> users = GitLabUserDriver.getUsers(getConfig().getGitlab());
            print(users);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getSshKeysWithUserIdTest() {
        try {
            List<SshKey> sshKeys = GitLabSshKeyDriver.getSshKeysWithUserId(getConfig().getGitlab(), BAIYI_USER_ID);
            print(sshKeys);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }


    /**
     * [
     * {
     * "sourceId":5,
     * "sourceName":"basic-service",
     * "sourceType":"Namespace",
     * "accessLevel":30
     * },
     * {
     * "sourceId":10,
     * "sourceName":"ops",
     * "sourceType":"Namespace",
     * "accessLevel":50
     * },
     * {
     * "sourceId":23,
     * "sourceName":"merchant-kili",
     * "sourceType":"Project",
     * "accessLevel":40
     * },
     * {
     * "sourceId":8,
     * "sourceName":"Android",
     * "sourceType":"Namespace",
     * "accessLevel":30
     * },
     * {
     * "sourceId":10,
     * "sourceName":"lib_net",
     * "sourceType":"Project",
     * "accessLevel":30
     * }
     * ]
     */
    @Test
    void getUserMembershipsTest() {
        try {
            List<Membership> memberships = GitLabUserDriver.getUserMemberships(getConfig().getGitlab(), BAIYI_USER_ID);
            print(memberships);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getMembersTest() {
        try {
            List<Member> members = GitLabProjectDriver.getMembersWithProjectId(getConfigById(56).getGitlab(), 73L, 20);
            print(members);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getProjectsTest() {
        try {
            List<Project> projects = GitLabProjectDriver.getProjects(getConfig().getGitlab());
            print(projects);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createUser() {
        try {
            gitlabUserDelegate.createUser(getConfigById(56).getGitlab(), "chenyingying");
        } catch (TicketProcessException e) {
             e.printStackTrace();
        }
    }

}
