package com.baiyi.opscloud.datasource.gitlab.driver.feature;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.factory.GitLabApiFactory;
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
    public static List<Member> getMembersWithGroupId(GitlabConfig.Gitlab gitlab, Long groupId) throws GitLabApiException {
        Pager<Member> memberPager = buildAPI(gitlab).getGroupApi().getMembers(groupId, ITEMS_PER_PAGE);
        return memberPager.all();
    }

    /**
     * 查询群组中所有项目
     *
     * @param gitlab
     * @return
     * @throws GitLabApiException
     */
    public static List<Project> getProjectsWithGroupId(GitlabConfig.Gitlab gitlab, Long groupId) throws GitLabApiException {
        Pager<Project> projectPager = buildAPI(gitlab).getGroupApi().getProjects(groupId, ITEMS_PER_PAGE);
        return projectPager.all();
    }

    public static void updateMember(GitlabConfig.Gitlab gitlab, Long groupId, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        buildAPI(gitlab).getGroupApi().updateMember(groupId, userId, accessLevel);
    }

    public static void addMember(GitlabConfig.Gitlab gitlab, Long groupId, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        buildAPI(gitlab).getProjectApi().addMember(groupId, userId, accessLevel);
    }

    /**
     * 查询GitLab实例中所有群组
     *
     * @param gitlab
     * @return
     * @throws GitLabApiException
     */
    public static List<Group> getGroups(GitlabConfig.Gitlab gitlab) throws GitLabApiException {
        return buildAPI(gitlab).getGroupApi().getGroups();
    }


    private static GitLabApi buildAPI(GitlabConfig.Gitlab gitlab) {
        return GitLabApiFactory.buildGitLabApi(gitlab);
    }

}
