package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.gitlab.GitlabWebHooksCommitsDO;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GitlabDao {

    int delWebHooks(@Param("id") long id);

    int addWebHooks(GitlabWebHooksDO webHooksDO);

    long getWebHooksSize(
            @Param("projectName") String projectName,
            @Param("repositoryName") String repositoryName,
            @Param("webHooksType") int webHooksType,
            @Param("triggerBuild") int triggerBuild);

    List<GitlabWebHooksDO> getWebHooksPage(
            @Param("projectName") String projectName,
            @Param("repositoryName") String repositoryName,
            @Param("webHooksType") int webHooksType,
            @Param("triggerBuild") int triggerBuild,
            @Param("pageStart") long pageStart, @Param("length") int length);

    GitlabWebHooksDO queryWebHooksById(@Param("id") long id);

    int delCommits(@Param("id") long id);

    int addCommits(GitlabWebHooksCommitsDO commitsDO);

    List<GitlabWebHooksCommitsDO> queryCommitsByWebHooksId(@Param("webHooksId") long webHooksId);

    List<GitlabWebHooksCommitsDO> queryCommitsByCommit(@Param("commitsId") String commitsId);


}
