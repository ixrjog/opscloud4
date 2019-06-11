package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.BusinessWrapper;

import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.gitlab.GitlabGroupDO;
import com.sdg.cmdb.domain.gitlab.GitlabGroupVO;
import com.sdg.cmdb.domain.gitlab.GitlabProjectVO;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;
import com.sdg.cmdb.domain.gitlab.v1.GitlabWebHooks;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailGitlabGroup;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailGitlabProject;
import org.gitlab.api.models.*;

import java.util.List;

public interface GitlabService {


    BusinessWrapper<Boolean> systemHooksV1(GitlabWebHooks webHooks);

    BusinessWrapper<Boolean> webHooksV1(GitlabWebHooks webHooks);

    TableVO<List<GitlabProjectVO>> getProjectPage(String name, String username, int page, int length);

    TableVO<List<GitlabWebHooksDO>> getWebHooksPage(String projectName, String ref, int triggerBuild, int page, int length);

    List<GitlabBranch> getProjectBranchs(int projectId);



    /**
     * 按Gitflow查询分支,过滤掉不合法的分支名称
     * @param projectId
     * @param envType
     * @return
     */
    List<GitlabBranch> getProjectBranchsByGitFlow(int projectId,int envType);

    GitlabBranch getProjectBranch(int projectId, String branch);

    GitlabVersion getVersion();



    /**
     * 持续集成中获取当前分支的Commit变更信息 最新的
     *
     * @param projectName
     * @param commitHash
     * @return
     */
    List<GitlabCommit> getProjectCommit2(String projectName, String commitHash);

    List<GitlabCommit> getChanges(long jobId, String jobName, String branch);


    //List<GitlabCommit> getProjectChanges(String projectName, String branch, String commitHash);


    // 新版本
    boolean updateProjcets();

    // 更新群组
    boolean updateGroups();

    List<GitlabSSHKey> getUserSSHKey(String username);

    /**
     * 推送公钥到Gitlab
     * @param username
     * @param sshkey
     * @return
     */
    boolean pushSSHKey(String username, String sshkey);
    boolean isSetKey(String username);
    boolean updateUsers();


    GitlabUser createUser(String username);


    List<GitlabGroupVO> queryGroup(String groupName);

    List<GitlabProjectVO> queryProject(String projectName);

    /**
     * 群组授权
     * @param gitlabGroup
     * @return
     */
    boolean groupAuth(TodoDetailGitlabGroup gitlabGroup,UserDO userDO);

    boolean projectAuth(TodoDetailGitlabProject gitlabProject, UserDO userDO);


}
