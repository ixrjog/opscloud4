package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.BusinessWrapper;

import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.gitlab.GitlabProjectVO;
import com.sdg.cmdb.domain.gitlab.v1.GitlabWebHooks;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabVersion;

import java.util.List;

public interface GitlabService {

    TableVO<List<GitlabProjectVO>> getProjectPage(String name, String username, int page, int length);

    List<GitlabBranch> getProjectBranchs(int projectId);

    GitlabBranch getProjectBranch(int projectId,String branch);

    GitlabVersion getVersion();

    BusinessWrapper<Boolean> webHooksV1(GitlabWebHooks webHooks);

    /**
     * 持续集成中获取当前分支的Commit变更信息 最新的
     *
     * @param projectName
     * @param commitHash
     * @return
     */
    List<GitlabCommit> getProjectCommit2(String projectName, String commitHash);

    List<GitlabCommit>  getChanges(long jobId, String jobName,String branch);


    //List<GitlabCommit> getProjectChanges(String projectName, String branch, String commitHash);


    // 新版本
    boolean updateProjcets();


}
