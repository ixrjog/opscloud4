package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.client.GitLabApiBuilder;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.Pager;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.Project;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/10/27 14:39
 * @Version 1.0
 */
@Slf4j
public class GitLabGroupDriver {

    public static final int ITEMS_PER_PAGE = 20;

    /**
     * 查询群组中所有成员
     *
     * @param gitlab
     * @param groupId
     * @return
     * @throws GitLabApiException
     */
    public static List<Member> getMembersWithGroupId(GitLabConfig.GitLab gitlab, Long groupId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            Pager<Member> memberPager = gitLabApi.getGroupApi().getMembers(groupId, ITEMS_PER_PAGE);
            return memberPager.all();
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 查询群组中所有项目
     *
     * @param gitlab
     * @return
     * @throws GitLabApiException
     */
    public static List<Project> getProjectsWithGroupId(GitLabConfig.GitLab gitlab, Long groupId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            Pager<Project> projectPager = gitLabApi.getGroupApi().getProjects(groupId, ITEMS_PER_PAGE);
            return projectPager.all();
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 修改群组成员
     *
     * @param gitlab
     * @param groupId
     * @param userId
     * @param accessLevel
     * @throws GitLabApiException
     */
    public static void updateMember(GitLabConfig.GitLab gitlab, Long groupId, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            gitLabApi.getGroupApi().updateMember(groupId, userId, accessLevel);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 新增群组成员
     *
     * @param gitlab
     * @param groupId
     * @param userId
     * @param accessLevel
     * @throws GitLabApiException
     */
    public static void addMember(GitLabConfig.GitLab gitlab, Long groupId, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            gitLabApi.getGroupApi().addMember(groupId, userId, accessLevel);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 查询GitLab实例中所有群组
     *
     * @param gitlab
     * @return
     * @throws GitLabApiException
     */
    public static List<Group> getGroups(GitLabConfig.GitLab gitlab) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getGroupApi().getGroups();
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}