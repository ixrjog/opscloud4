package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitLabApiFactory;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.Pager;
import org.gitlab4j.api.models.*;

import java.util.List;
import java.util.Optional;

/**
 * https://docs.gitlab.com/ee/api/projects.html
 *
 * @Author baiyi
 * @Date 2022/10/26 15:32
 * @Version 1.0
 */
@Slf4j
public class GitLabProjectDriver {

    public static final int ITEMS_PER_PAGE = 20;

    /**
     * 查询项目中所有成员
     *
     * @param gitlab
     * @param projectId
     * @param itemsPerPage 分页查询长度
     * @return
     * @throws GitLabApiException
     */
    public static List<Member> getMembersWithProjectId(GitLabConfig.Gitlab gitlab, Long projectId, int itemsPerPage) throws GitLabApiException {
        Pager<Member> memberPager = buildAPI(gitlab).getProjectApi().getMembers(projectId, itemsPerPage);
        return memberPager.all();
    }

    /**
     * 查询项目中所有成员
     *
     * @param gitlab
     * @param projectId
     * @return
     * @throws GitLabApiException
     */
    public static List<Member> getMembersWithProjectId(GitLabConfig.Gitlab gitlab, Long projectId) throws GitLabApiException {
        Pager<Member> memberPager = buildAPI(gitlab).getProjectApi().getMembers(projectId, ITEMS_PER_PAGE);
        return memberPager.all();
    }

    /**
     * 修改项目成员
     *
     * @param gitlab
     * @param projectId
     * @param userId
     * @param accessLevel
     * @throws GitLabApiException
     */
    public static void updateMember(GitLabConfig.Gitlab gitlab, Long projectId, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        buildAPI(gitlab).getProjectApi().updateMember(projectId, userId, accessLevel);
    }

    /**
     * 新增项目成员
     *
     * @param gitlab
     * @param projectId
     * @param userId
     * @param accessLevel
     * @throws GitLabApiException
     */
    public static void addMember(GitLabConfig.Gitlab gitlab, Long projectId, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        buildAPI(gitlab).getProjectApi().addMember(projectId, userId, accessLevel);
    }

    /**
     * 查询GitLab实例中所有项目
     *
     * @param gitlab
     * @return
     * @throws GitLabApiException
     */
    public static List<Project> getProjects(GitLabConfig.Gitlab gitlab) throws GitLabApiException {
        return buildAPI(gitlab).getProjectApi().getProjects();
    }

    public static List<Tag> getTagsWithProjectId(GitLabConfig.Gitlab gitlab, Long projectId) throws GitLabApiException {
        return buildAPI(gitlab).getTagsApi().getTags(projectId);
    }

    public static Optional<Tag> getTagWithProjectIdAndTagName(GitLabConfig.Gitlab gitlab, Long projectId, String tagName) throws GitLabApiException {
        return buildAPI(gitlab).getTagsApi().getOptionalTag(projectId, tagName);
    }

    public static List<Branch> getBranchesWithProjectId(GitLabConfig.Gitlab gitlab, Long projectId) throws GitLabApiException {
        return buildAPI(gitlab).getRepositoryApi().getBranches(projectId);
    }

    public static Optional<Branch> getBranchWithProjectIdAndBranchName(GitLabConfig.Gitlab gitlab, Long projectId, String branchName) throws GitLabApiException {
        return buildAPI(gitlab).getRepositoryApi().getOptionalBranch(projectId, branchName);
    }

    private static GitLabApi buildAPI(GitLabConfig.Gitlab gitlab) {
        return GitLabApiFactory.buildGitLabApi(gitlab);
    }

}
