package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.sdg.cmdb.dao.cmdb.CiDao;
import com.sdg.cmdb.dao.cmdb.GitlabDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.PublicType;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.ci.CiAppDO;
import com.sdg.cmdb.domain.ci.CiBuildDO;
import com.sdg.cmdb.domain.gitlab.*;
import com.sdg.cmdb.domain.gitlab.v1.GitlabWebHooks;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailGitlabGroup;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailGitlabProject;
import com.sdg.cmdb.plugin.gitlab.GitlabFactory;
import com.sdg.cmdb.service.*;

import com.sdg.cmdb.util.BeanCopierUtils;
import com.sdg.cmdb.util.GitFlowUtils;
import com.sdg.cmdb.util.PasswdUtils;
import com.sdg.cmdb.util.SSHKeyUtils;

import org.gitlab.api.Pagination;
import org.gitlab.api.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
public class GitlabServiceImpl implements GitlabService {

    @Autowired
    private GitlabFactory gitlabFactory;

    @Autowired
    private GitlabDao gitlabDao;

    @Autowired
    private CiDao ciDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LdapService ldapService;

    @Autowired
    private CiService ciService;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheKeyService cacheKeyService;

    public static final String CACHE_KEY = "GitlabServiceImpl:";

    public static final String BRANCH_REF = "refs/heads/";

    public GitlabVersion getVersion() {
        try {
            return gitlabFactory.getApi().getVersion();
        } catch (Exception e) {
            return new GitlabVersion();
        }
    }

    public static final String[] GROUP_EVENTS = {
            "group_create",
            "group_destroy",
            "group_rename",
            "user_add_to_group",
            "user_remove_from_group"};

    public static final String[] USER_EVENTS = {
            "user_add_to_team",
            "user_remove_from_team",
            "user_create",
            "user_destroy",
            "user_failed_login",
            "user_rename"};

    public static final String[] PROJECT_EVENTS = {
            "project_create",
            "project_destroy",
            "project_rename",
            "project_transfer",
            "project_update"};

    @Override
    public BusinessWrapper<Boolean> systemHooksV1(GitlabWebHooks webHooks) {
        try {
            if (isEvent(webHooks.getEvent_name(), GROUP_EVENTS)) {
                updateGroups();
            } else if (isEvent(webHooks.getEvent_name(), USER_EVENTS)) {
                updateUsers();
            } else if (isEvent(webHooks.getEvent_name(), PROJECT_EVENTS)) {
                updateProjcets();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<Boolean>(true);
    }

    private boolean isEvent(String eventName, String[] events) {
        for (String event : events)
            if (event.equals(eventName)) return true;
        return false;
    }

    @Override
    public BusinessWrapper<Boolean> webHooksV1(GitlabWebHooks webHooks) {
        try {
            if (!webHooks.getEvent_name().equals(PublicType.HookEventTypeEnum.push.getDesc()))
                return new BusinessWrapper<Boolean>(true);
            GitlabWebHooksDO gitlabWebHooksDO = new GitlabWebHooksDO(webHooks);
            gitlabDao.addWebHooks(gitlabWebHooksDO);
            hookTriggerBuild(gitlabWebHooksDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<Boolean>(true);
    }

    /**
     * 校验是否触发构建
     *
     * @param gitlabWebHooksDO
     */
    private void hookTriggerBuild(GitlabWebHooksDO gitlabWebHooksDO) {
        if (gitlabWebHooksDO == null || StringUtils.isEmpty(gitlabWebHooksDO.getRef()))
            return;
        ciService.autoBuild(gitlabWebHooksDO);
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
    public TableVO<List<GitlabWebHooksDO>> getWebHooksPage(String projectName, String ref, int triggerBuild, int page, int length) {
        long size = gitlabDao.getWebHooksSize(projectName, ref, triggerBuild);
        List<GitlabWebHooksDO> list = gitlabDao.getWebHooksPage(projectName, ref, triggerBuild, page * length, length);
        for (GitlabWebHooksDO gitlabWebHooksDO : list) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Object body = mapper.readValue(gitlabWebHooksDO.getWebhooksBody(), Object.class);
                gitlabWebHooksDO.setWebhooksBody(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body));
            } catch (Exception e) {
            }
        }

        return new TableVO<>(size, list);
    }

    private String getProjectBranchCacheKey(int projectId) {
        return CACHE_KEY + "projectId:" + projectId;
    }

    @Override
    public List<GitlabBranch> getProjectBranchs(int projectId) {
        String key = getProjectBranchCacheKey(projectId);
        String cache = cacheKeyService.getKeyByString(key);
        List<GitlabBranch> branchs = new ArrayList<>();
        if (!StringUtils.isEmpty(cache)) {
            GsonBuilder builder = new GsonBuilder();
            // Register an adapter to manage the date types as long values
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            branchs = gson.fromJson(cache, new TypeToken<ArrayList<GitlabBranch>>() {
            }.getType());
            return branchs;
        }
        branchs = gitlabFactory.getApi().getBranches(projectId);
        cacheKeyService.set(key, JSON.toJSONString(branchs), 5);
        return branchs;
    }

    @Override
    public List<GitlabBranch> getProjectBranchsByGitFlow(int projectId, int envType) {
        String key =getProjectBranchCacheKey(projectId) + ":envType:" + envType;
        String cache = cacheKeyService.getKeyByString(key);
        List<GitlabBranch> branchs = new ArrayList<>();
        if (!StringUtils.isEmpty(cache)) {
            GsonBuilder builder = new GsonBuilder();
            // Register an adapter to manage the date types as long values
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            branchs = gson.fromJson(cache, new TypeToken<ArrayList<GitlabBranch>>() {
            }.getType());
            return branchs;
        }
        branchs = getProjectBranchs(projectId);
        List<GitlabBranch> gitFlowBranchs = GitFlowUtils.filterBranchs(branchs, envType);
        cacheKeyService.set(key, JSON.toJSONString(gitFlowBranchs), 2);

        return gitFlowBranchs;
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
        List<GitlabBranch> branchs = new ArrayList<>();
        try {
            branchs = gitlabFactory.getApi().getBranches(gitlabProjectDO.getProjectId());
        } catch (Exception e) {
        }
        List<GitlabTag> tags = new ArrayList<>();
        try {
            tags = gitlabFactory.getApi().getTags(gitlabProjectDO.getProjectId());
        } catch (Exception e) {
        }
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
        List<GitlabProjectDO> gitlabProjectList = gitlabDao.getGitlabProjectPage("", "", 0, 10000);
        HashMap<Integer, GitlabProjectDO> map = new HashMap<>();
        for (GitlabProjectDO gitlabProjectDO : gitlabProjectList)
            map.put(gitlabProjectDO.getProjectId(), gitlabProjectDO);
        List<GitlabProject> projects = gitlabFactory.getApi().getAllProjects();
        for (GitlabProject project : projects) {
            if (map.containsKey(project.getId()))
                map.remove(project.getId());
            if (!saveProject(project))
                return false;
        }
        for (Integer projectId : map.keySet())
            gitlabDao.delGitlabProject(projectId);
        return true;
    }

    @Override
    public boolean updateGroups() {
        try {
            List<GitlabGroupDO> gitlabGroupList = gitlabDao.getGitlabGroupPage("", 0, 1000);
            HashMap<Integer, GitlabGroupDO> map = new HashMap<>();
            for (GitlabGroupDO gitlabGroupDO : gitlabGroupList)
                map.put(gitlabGroupDO.getGroupId(), gitlabGroupDO);
            List<GitlabGroup> groups = gitlabFactory.getApi().getGroups();
            for (GitlabGroup group : groups) {
                if (map.containsKey(group.getId()))
                    map.remove(group.getId());
                if (!saveGroup(group))
                    return false;
            }
            for (Integer groupId : map.keySet())
                gitlabDao.delGitlabGroup(groupId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveGroup(GitlabGroup group) {
        GitlabGroupDO gitlabGroupDO = gitlabDao.getGitlabGroupByGroupId(group.getId());
        try {
            if (gitlabGroupDO == null) {
                gitlabDao.addGitlabGroup(new GitlabGroupDO(group));
            } else {
                gitlabDao.updateGitlabGroup(new GitlabGroupDO(gitlabGroupDO.getId(), group));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<GitlabSSHKey> getUserSSHKey(String username) {
        GitlabUserDO gitlabUserDO = gitlabDao.getGitlabUserByUsername(username);
        if (gitlabUserDO == null) return new ArrayList<>();
        try {
            List<GitlabSSHKey> sshKeyList = gitlabFactory.getApi().getSSHKeys(gitlabUserDO.getGitlabUserId());
            return sshKeyList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean pushSSHKey(String username, String sshkey) {
        String keyHash = SSHKeyUtils.getMD5(sshkey);
        if (StringUtils.isEmpty(keyHash)) return false;
        GitlabUserDO gitlabUserDO = gitlabDao.getGitlabUserByUsername(username);
        if (gitlabUserDO == null) {
            gitlabUserDO = createGitlabUser(username);
        } else {
            String userKeyHash = SSHKeyUtils.getMD5(gitlabUserDO.getSetKey());
            if (keyHash.equals(userKeyHash))
                return true;
        }
        List<GitlabSSHKey> sshKeyList = getUserSSHKey(username);
        for (GitlabSSHKey gitlabSSHKey : sshKeyList) {
            if (SSHKeyUtils.getMD5(gitlabSSHKey.getKey()).equals(keyHash)) {
                gitlabUserDO.setSetKey(keyHash);
                gitlabDao.updateGitlabUser(gitlabUserDO);
                return true;
            }
        }
        try {
            GitlabSSHKey gitlabSSHKey = gitlabFactory.getApi().createSSHKey(gitlabUserDO.getGitlabUserId(), "By opscloud", sshkey);
            if (gitlabSSHKey == null) return false;
            gitlabUserDO.setSetKey(keyHash);
            gitlabDao.updateGitlabUser(gitlabUserDO);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private GitlabUserDO createGitlabUser(String username) {
        updateUsers();
        GitlabUserDO gitlabUserDO = gitlabDao.getGitlabUserByUsername(username);
        if (gitlabUserDO != null) return gitlabUserDO;
        GitlabUser gitlabUser = createUser(username);
        UserDO userDO = userService.getUserDOByName(gitlabUser.getUsername());
        if (userDO == null) return null;
        gitlabUserDO = new GitlabUserDO(gitlabUser, userDO.getId());
        gitlabDao.addGitlabUser(gitlabUserDO);
        return gitlabUserDO;
    }

    @Override
    public boolean isSetKey(String username) {
        GitlabUserDO gitlabUserDO = gitlabDao.getGitlabUserByUsername(username);
        if (gitlabUserDO == null) return false;
        if (StringUtils.isEmpty(gitlabUserDO.getSetKey()))
            return false;
        return true;
    }


    @Override
    public boolean updateUsers() {
        List<GitlabUser> users = gitlabFactory.getApi().getUsers();
        for (GitlabUser gitlabUser : users) {
            GitlabUserDO gitlabUserDO = gitlabDao.getGitlabUserByUsername(gitlabUser.getUsername());
            if (gitlabUserDO == null) {
                UserDO userDO = userService.getUserDOByName(gitlabUser.getUsername());
                if (userDO == null) continue; // 未匹配到用户则不录入
                gitlabUserDO = new GitlabUserDO(gitlabUser, userDO.getId());
                gitlabDao.addGitlabUser(gitlabUserDO);
            }
        }
        return true;
    }

    @Override
    public GitlabUser createUser(String username) {
        UserDO userDO = userService.getUserDOByName(username);
        if (userDO == null) return new GitlabUser();
        try {
            /**
             * Create a new User
             * @param email                User email
             * @param password             Password
             * @param username             User name
             * @param fullName             Full name
             * @param skypeId              Skype Id
             * @param linkedIn             LinkedIn
             * @param twitter              Twitter
             * @param website_url          Website URL
             * @param projects_limit       Projects limit
             * @param extern_uid           External User ID
             * @param extern_provider_name External Provider Name
             * @param bio                  Bio
             * @param isAdmin              Is Admin
             * @param can_create_group     Can Create Group
             * @param skip_confirmation    Skip Confirmation
             * @param external             External
             * @return A GitlabUser
             * @throws IOException on gitlab api call error
             * @see <a href="http://doc.gitlab.com/ce/api/users.html">http://doc.gitlab.com/ce/api/users.html</a>
             */
            return gitlabFactory.getApi().createUser(userDO.getMail(), PasswdUtils.getPassword(20), userDO.getUsername(),
                    userDO.getDisplayName(), "", "", "", "", 100000, ldapService.getUserDN(username), "ldapmain", "", false, true, true, false);
        } catch (IOException e) {
            e.printStackTrace();
            return new GitlabUser();
        }
    }

    private boolean saveProject(GitlabProject project) {
        GitlabProjectDO gitlabProjectDO = gitlabDao.getGitlabProjectByProjectId(project.getId());
        try {
            if (gitlabProjectDO == null) {
                gitlabDao.addGitlabProject(new GitlabProjectDO(project));
            } else {
                gitlabDao.updateGitlabProject(new GitlabProjectDO(gitlabProjectDO.getId(), project));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    /**
     * 重新处理，计算上一次构建commit
     *
     * @param jobId
     * @param jobName
     * @param branch
     * @return
     */
    @Override
    public List<GitlabCommit> getChanges(long jobId, String jobName, String branch) {
        CiAppDO ciAppDO = ciService.getCiAppByJobId(jobId);
        GitlabProjectDO gitlabProjectDO = gitlabDao.getGitlabProjectByName(ciAppDO.getProjectName());
        try {
            GitlabBranch gitlabBranch = gitlabFactory.getApi().getBranch(gitlabProjectDO.getProjectId(), branch);
            CiBuildDO ciBuildDO = ciDao.getLastBuildByJobId(jobId);
            String startCommit = "";
            if (ciBuildDO != null)
                startCommit = ciBuildDO.getCommit();
            String finalizeCommit = gitlabBranch.getCommit().getId();
            return getChanges(startCommit, finalizeCommit, gitlabProjectDO.getProjectId(), branch);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<GitlabCommit> getChanges(String startCommit, String finalizeCommit, int gitlabProjectId, String branch) {
        Pagination pagination = new Pagination();
        pagination.setPage(0);
        pagination.setPerPage(100);
        boolean first = true;
        List<GitlabCommit> list = new ArrayList<>();
        String endCommit = finalizeCommit;

        try {
            List<GitlabCommit> commits = gitlabFactory.getApi().getCommits(gitlabProjectId, pagination, branch);
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

    @Override
    public List<GitlabGroupVO> queryGroup(String groupName) {
        List<GitlabGroupDO> list = gitlabDao.getGitlabGroupPage(groupName, 0, 15);
        List<GitlabGroupVO> voList = new ArrayList<>();
        for (GitlabGroupDO gitlabGroupDO : list) {
            GitlabGroupVO groupVO = BeanCopierUtils.copyProperties(gitlabGroupDO, GitlabGroupVO.class);
            groupVO.setMemberList(getGroupMembersByOwner(gitlabGroupDO.getGroupId()));
            try {
                groupVO.setProjectList(gitlabFactory.getApi().getGroupProjects(gitlabGroupDO.getGroupId()));
            } catch (Exception e) {
            }
            voList.add(groupVO);
        }
        return voList;
    }

    @Override
    public List<GitlabProjectVO> queryProject(String projectName) {
        List<GitlabProjectDO> list = gitlabDao.getGitlabProjectPage(projectName, "", 0, 15);
        List<GitlabProjectVO> voList = new ArrayList<>();
        for (GitlabProjectDO gitlabProjectDO : list) {
            GitlabProjectVO gitlabProjectVO = getGitlabProjectVO(gitlabProjectDO);
            try {
                gitlabProjectVO.setMemberList(getProjectMembersByMaster(gitlabProjectDO.getProjectId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            voList.add(gitlabProjectVO);
        }
        return voList;
    }

    @Override
    public boolean groupAuth(TodoDetailGitlabGroup gitlabGroup, UserDO userDO) {
        GitlabAccessLevel gitlabAccessLevel = GitlabAccessLevel.fromAccessValue(GitlabAccessLevel.valueOf(gitlabGroup.getAccessLevel()).accessValue);
        //updateUsers(); // 同步用户
        GitlabUserDO gitlabUserDO = gitlabDao.getGitlabUserByUsername(userDO.getUsername());

        List<GitlabGroupMember> memberList = gitlabFactory.getApi().getGroupMembers(gitlabGroup.getGroupId());
        for (GitlabGroupMember member : memberList) {
            // 用户已存在需要先删除
            if (member.getUsername().equals(userDO.getUsername())) {
                try {
                    gitlabFactory.getApi().deleteGroupMember(gitlabGroup.getGroupId(), gitlabUserDO.getGitlabUserId());
                    break;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        try {
            GitlabGroupMember member = gitlabFactory.getApi().addGroupMember(gitlabGroup.getGroupId(), gitlabUserDO.getGitlabUserId(), gitlabAccessLevel);
            if (member != null && !StringUtils.isEmpty(member.getUsername())) return true;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean projectAuth(TodoDetailGitlabProject gitlabProject, UserDO userDO) {
        GitlabAccessLevel gitlabAccessLevel = GitlabAccessLevel.fromAccessValue(GitlabAccessLevel.valueOf(gitlabProject.getAccessLevel()).accessValue);
        GitlabUserDO gitlabUserDO = gitlabDao.getGitlabUserByUsername(userDO.getUsername());
        try {
            List<GitlabProjectMember> memberList = gitlabFactory.getApi().getProjectMembers(gitlabProject.getProjectId());
            for (GitlabProjectMember member : memberList) {
                // 用户已存在则更新
                if (member.getUsername().equals(userDO.getUsername())) {
                    // 更新成员
                    gitlabFactory.getApi().updateProjectMember(gitlabProject.getProjectId(), gitlabUserDO.getGitlabUserId(), gitlabAccessLevel);
                    return true;
                }
            }
            // 添加成员
            gitlabFactory.getApi().addProjectMember(gitlabProject.getProjectId(), gitlabUserDO.getGitlabUserId(), gitlabAccessLevel);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 取群组中的所有者
     *
     * @param groupId
     * @return
     */
    private List<GitlabGroupMember> getGroupMembersByOwner(int groupId) {
        List<GitlabGroupMember> members = gitlabFactory.getApi().getGroupMembers(groupId);
        List<GitlabGroupMember> memberList = new ArrayList<>();
        for (GitlabGroupMember member : members) {
            if (member.getAccessLevel().name().equals(GitlabAccessLevel.Owner.name()))
                memberList.add(member);
        }
        return memberList;
    }

    private List<GitlabProjectMember> getProjectMembersByMaster(int projectId) {
        List<GitlabProjectMember> memberList = new ArrayList<>();
        try {
            List<GitlabProjectMember> members = gitlabFactory.getApi().getProjectMembers(projectId);
            for (GitlabProjectMember member : members) {
                if (member.getAccessLevel().name().equals(GitlabAccessLevel.Master.name()))
                    memberList.add(member);
            }
        } catch (Exception e) {
        }
        return memberList;
    }


}
