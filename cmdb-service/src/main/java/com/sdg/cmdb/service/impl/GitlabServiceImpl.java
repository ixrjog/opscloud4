package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.CiDao;
import com.sdg.cmdb.dao.cmdb.GitlabDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.ci.CiAppDO;
import com.sdg.cmdb.domain.ci.CiJobDO;
import com.sdg.cmdb.domain.gitlab.GitlabProjectDO;
import com.sdg.cmdb.domain.gitlab.GitlabProjectVO;
import com.sdg.cmdb.domain.gitlab.v1.GitlabWebHooks;
import com.sdg.cmdb.plugin.gitlab.GitlabFactory;
import com.sdg.cmdb.service.GitlabService;

import org.gitlab.api.Pagination;
import org.gitlab.api.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class GitlabServiceImpl implements GitlabService {



    @Autowired
    private GitlabFactory gitlabFactory;

    @Resource
    private GitlabDao gitlabDao;

    @Resource
    private CiDao ciDao;

    @Resource
    private UserDao userDao;

    public GitlabVersion getVersion() {
        try {
            return gitlabFactory.getApi().getVersion();
        } catch (Exception e) {
            return new GitlabVersion();
        }
    }

    @Override
    public TableVO<List<GitlabProjectVO>> getProjectPage(String name, String username, int page, int length) {
        long size = gitlabDao.getGitlabProjectSize(name, username);
        List<GitlabProjectDO> list = gitlabDao.getGitlabProjectPage(name, username, page * length, length);
        List<GitlabProjectVO> voList = new ArrayList<>();
        for (GitlabProjectDO gitlabProjectDO : list) {
            voList.add(getGitlabProjectVO(gitlabProjectDO));

        }
        return new TableVO<>(size, voList);
    }

    @Override
    public List<GitlabBranch> getProjectBranchs(int projectId) {
        List<GitlabBranch> branchs = gitlabFactory.getApi().getBranches(projectId);
        return branchs;
    }

    @Override
    public GitlabBranch getProjectBranch(int projectId, String branch) {
        List<GitlabBranch> branchs = getProjectBranchs(projectId);
        for (GitlabBranch gitlabBranch : branchs)
            if (gitlabBranch.getName().equals(branch))
                return gitlabBranch;
        return null;
    }

    private GitlabProjectVO getGitlabProjectVO(GitlabProjectDO gitlabProjectDO) {
        List<GitlabTag> tags = gitlabFactory.getApi().getTags(gitlabProjectDO.getProjectId());
        List<GitlabBranch> branchs = gitlabFactory.getApi().getBranches(gitlabProjectDO.getProjectId());
        UserDO userDO = userDao.getUserByName(gitlabProjectDO.getOwnerUsername());
        if (userDO == null)
            userDO = new UserDO(gitlabProjectDO.getOwnerUsername());
        GitlabProjectVO gitlabProjectVO = new GitlabProjectVO(gitlabProjectDO, new UserVO(userDO, true), branchs, tags);
        return gitlabProjectVO;
    }


    /**
     * 从Gitlab更新所有Project
     *
     * @return
     */
    @Override
    public boolean updateProjcets() {
        List<GitlabProject> projects = gitlabFactory.getApi().getAllProjects();
        for (GitlabProject project : projects) {
            if (!saveProject(project))
                return false;
        }
        return true;
    }

    private boolean saveProject(GitlabProject project) {
        GitlabProjectDO gitlabProjectDO = gitlabDao.getGitlabProjectByProjectId(project.getId());
        try {
            if (gitlabProjectDO == null) {
                gitlabDao.addGitlabProject(new GitlabProjectDO(project));
            } else {
                gitlabDao.updateGitlabProject(new GitlabProjectDO(project));
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public BusinessWrapper<Boolean> webHooksV1(GitlabWebHooks webHooks) {
        return new BusinessWrapper<Boolean>(true);
    }


    @Override
    public List<GitlabCommit> getProjectCommit2(String projectName, String commitHash) {
        GitlabProjectDO gitlabProjectDO = gitlabDao.getGitlabProjectByName(projectName);
        List<GitlabCommit> list = new ArrayList<>();
        try {
            HashMap<String, GitlabCommit> commitMap = new HashMap<>();
            GitlabCommit commit = getCommit(gitlabProjectDO.getProjectId(), commitHash);
            if (!isMerge(commit)) {
                commitMap.put(commit.getId(), commit);
            }
            for (String parentCommitHash : commit.getParentIds()) {
                GitlabCommit parentCommit = getCommit(gitlabProjectDO.getProjectId(), parentCommitHash);
                if (!isMerge(parentCommit))
                    commitMap.put(parentCommit.getId(), parentCommit);
            }
            for (String key : commitMap.keySet())
                list.add(commitMap.get(key));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<GitlabCommit> getChanges(long jobId, String jobName, String branch) {
        String startCommit = ciDao.queryNotifyStartedByJobName(jobName);
        String finalizeCommit = ciDao.queryNotifyFinalizedByJobName(jobName);
        Pagination pagination = new Pagination();
        pagination.setPage(0);
        pagination.setPerPage(100);
        CiJobDO ciJobDO = ciDao.getCiJob(jobId);
        CiAppDO ciAppDO = ciDao.getCiApp(ciJobDO.getAppId());
        GitlabProjectDO gitlabProjectDO = gitlabDao.getGitlabProjectByName(ciAppDO.getProjectName());
        boolean first = true;
        List<GitlabCommit> list = new ArrayList<>();
        String endCommit = finalizeCommit;

        try {

            List<GitlabCommit> commits = gitlabFactory.getApi().getCommits(gitlabProjectDO.getProjectId(), pagination, branch);
            for (GitlabCommit commit : commits) {
                if (first && commit.getId().equals(finalizeCommit)) {
                    endCommit = startCommit;
                }
                if (commit.getId().equals(endCommit)) {
                    break;
                } else {
                    list.add(commit);
                }
                first = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private GitlabCommit getCommit(int projectId, String commitHash) {
        try {
            GitlabCommit commit = gitlabFactory.getApi().getCommit(projectId, commitHash);
            return commit;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isMerge(GitlabCommit commit) {
        if (commit == null) return true;
        if (commit.getMessage().indexOf("Merge branch", 0) == 0) {
            return true;
        } else {
            return false;
        }
    }


}
