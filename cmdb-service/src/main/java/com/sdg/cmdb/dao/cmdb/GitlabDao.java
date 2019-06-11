package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.gitlab.GitlabGroupDO;
import com.sdg.cmdb.domain.gitlab.GitlabProjectDO;
import com.sdg.cmdb.domain.gitlab.GitlabUserDO;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GitlabDao {

    int delWebHooks(@Param("id") long id);

    int addWebHooks(GitlabWebHooksDO webHooksDO);

    int updateWebHooks(GitlabWebHooksDO webHooksDO);

    long getWebHooksSize(
            @Param("projectName") String projectName,
            @Param("ref") String repositoryName,
            @Param("triggerBuild") int triggerBuild);

    List<GitlabWebHooksDO> getWebHooksPage(
            @Param("projectName") String projectName,
            @Param("ref") String repositoryName,
            @Param("triggerBuild") int triggerBuild,
            @Param("pageStart") long pageStart, @Param("length") int length);

    GitlabWebHooksDO queryWebHooksById(@Param("id") long id);


    int addGitlabProject(GitlabProjectDO gitlabProjectDO);

    int updateGitlabProject(GitlabProjectDO gitlabProjectDO);
    int delGitlabProject(@Param("projectId") int projectId);

    GitlabProjectDO getGitlabProjectByProjectId(@Param("projectId") int projectId);

    GitlabProjectDO getGitlabProjectByName(@Param("name") String name);

    long getGitlabProjectSize(
            @Param("name") String name,
            @Param("username") String username);

    List<GitlabProjectDO> getGitlabProjectPage(
            @Param("name") String name,
            @Param("username") String username,
            @Param("pageStart") long pageStart, @Param("length") int length);

    int addGitlabGroup(GitlabGroupDO gitlabGroupDO);
    int delGitlabGroup(@Param("groupId") int groupId);

    int updateGitlabGroup(GitlabGroupDO gitlabGroupDO);
    GitlabGroupDO getGitlabGroupByGroupId(@Param("groupId") int groupId);
    long getGitlabGroupSize(
            @Param("name") String name);

    List<GitlabGroupDO> getGitlabGroupPage(
            @Param("name") String name,
            @Param("pageStart") long pageStart, @Param("length") int length);


    int addGitlabUser(GitlabUserDO gitlabUserDO);

    int updateGitlabUser(GitlabUserDO gitlabUserDO);

    GitlabUserDO getGitlabUserByUsername(@Param("username") String username);

    int delGitlabUser(@Param("id") String id);

}
