package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.JenkinsDao;
import com.sdg.cmdb.dao.cmdb.ScmDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.dao.stash.StashDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.CiUserGroupDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.JenkinsItemEnum;
import com.sdg.cmdb.domain.gitlab.RefsDO;
import com.sdg.cmdb.domain.gitlab.RefsVO;
import com.sdg.cmdb.domain.gitlab.RepositoryDO;
import com.sdg.cmdb.domain.scm.ScmPermissionsDO;
import com.sdg.cmdb.domain.scm.ScmPermissionsVO;
import com.sdg.cmdb.domain.todo.todoProperty.StashProjectDO;
import com.sdg.cmdb.domain.todo.todoProperty.StashRepositoryDO;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.GitService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


@Service
public class GitServiceImpl implements GitService {

    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");
    private static final Logger logger = LoggerFactory.getLogger(GitService.class);


    // gitlab repository
    public static final int GITLAB_REPOSITORY = 0;
    // stash repository
    public static final int STASH_REPOSITORY = 1;
    // 分支类型
    private static final String TAGS_PREFIX = "refs/tags/";
    private static final String BRANCHES_PREFIX = "refs/remotes/origin/";

    @Resource
    private JenkinsDao jenkinsDao;

    @Resource
    private StashDao stashDao;

    @Resource
    private ScmDao scmDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ConfigCenterService configCenterService;

    private HashMap<String, String> configMap;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.JENKINS.getItemKey());
    }

    /**
     * 删除文件夹及其下的内容
     *
     * @param file
     */
    public static void delDir(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File tmpFile : file.listFiles()) {
                    delDir(tmpFile);
                }
                file.delete();
            } else {
                file.delete();
            }
        } else {
            try {
                // 强制删除link类型文件
                file.delete();
            } catch (Exception e) {

            }
        }

    }


    /**
     * 获取指定仓库的所有分支
     *
     * @param repositoryType
     * @param project
     * @param repo
     * @return
     * @throws Exception
     */
    @Override
    public RefsVO getRefList(int repositoryType, String project, String repo) throws Exception {
        HashMap<String, String> configMap = acqConifMap();
        String localPath = configMap.get(JenkinsItemEnum.REPO_LOCAL_PATH.getItemKey());
        //String localPath = "~/Documents/cmdb";
        File projectPath = new File(localPath + "/" + project);
        File localFile = new File(localPath + "/" + project + "/" + repo);
        coreLogger.info("Clear local repository project:{} ; repo : {} ,path", project, repo, localFile.getAbsolutePath());
        //System.err.println(localFile);
        delDir(localFile);
        //System.err.println(projectPath);
        delDir(projectPath);

        String repositoryUrl = acqRepositoryUrl(repositoryType, project, repo);
        String user = "";
        String pwd = "";
        //  http://host/xq_ios/lianhua.git
        if (repositoryType == GITLAB_REPOSITORY) {
            user = configMap.get(JenkinsItemEnum.GITLAB_USER.getItemKey());
            pwd = configMap.get(JenkinsItemEnum.GITLAB_PWD.getItemKey());
        }
        // http://host/scm/xq_ios/lianhua.git
        if (repositoryType == STASH_REPOSITORY) {
            user = configMap.get(JenkinsItemEnum.STASH_USER.getItemKey());
            pwd = configMap.get(JenkinsItemEnum.STASH_PWD.getItemKey());
        }

        Git git = getGitRepositoryRefList(repositoryUrl, localFile, user, pwd);
        Set<String> refList = git.getRepository().getAllRefs().keySet();
        //System.err.println(refList);
        return getRefs(refList);
    }

    /**
     * 获取Git仓库地址
     *
     * @param repositoryType
     * @param project
     * @param repo
     * @return
     */
    private String acqRepositoryUrl(int repositoryType, String project, String repo) {
        HashMap<String, String> configMap = acqConifMap();
        String repositoryUrl = "";
        if (repositoryType == GITLAB_REPOSITORY) {
            repositoryUrl = configMap.get(JenkinsItemEnum.GITLAB_HOST.getItemKey());
            repositoryUrl += "/" + project + "/" + repo + ".git";
        }
        // http://host/scm/xq_ios/lianhua.git
        if (repositoryType == STASH_REPOSITORY) {
            repositoryUrl = configMap.get(JenkinsItemEnum.STASH_HOST.getItemKey());
            StashProjectDO stashProjectDO = stashDao.getStashProjectByName(project);
            if (stashProjectDO != null) {
                repositoryUrl += "/scm/" + stashProjectDO.getProject_key() + "/" + repo + ".git";
            } else {
                repositoryUrl += "/scm/" + project + "/" + repo + ".git";
            }

        }
        return repositoryUrl;
    }


    /**
     * 获取分支和tag列表
     *
     * @param refList
     * @return
     */
    private RefsVO getRefs(Set<String> refList) {
        List<String> branches = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        try {
            Object[] refs = refList.toArray();
            for (Object refObject : refs) {
                String ref = (String) refObject;
                if (ref.indexOf(BRANCHES_PREFIX) != -1)
                    branches.add(ref.replace(BRANCHES_PREFIX, ""));
                if (ref.indexOf(TAGS_PREFIX) != -1)
                    tags.add(ref.replace(TAGS_PREFIX, ""));
            }
            RefsVO refsVO = new RefsVO(branches, tags);
            return refsVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RefsVO();
    }

    private Git getGitRepositoryRefList(String repositoryUrl, File localFile, String user, String pwd) throws Exception {
        //System.err.println("repositoryUrl:"+repositoryUrl);
        //System.err.println("localFile:"+localFile);
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(user, pwd);
        return Git.cloneRepository()
                .setURI(repositoryUrl)
                .setDirectory(localFile)
                .setCredentialsProvider(credentialsProvider)
                .call();
    }

    @Override
    public RefsVO queryRefs(int repositoryType, String project, String repo) {
        String repositoryUrl = acqRepositoryUrl(repositoryType, project, repo);
        RepositoryDO repositoryDO = jenkinsDao.queryRepositoryByUrl(repositoryUrl);
        //System.err.println(repositoryDO);
        if (repositoryDO == null)
            return getRefs(repositoryType, project, repo);
        return getRefs(repositoryDO);
    }

    private RefsVO getRefs(RepositoryDO repositoryDO) {
        List<RefsDO> list = jenkinsDao.queryRefsByRepoId(repositoryDO.getId());
        return acqRefsVO(list);
    }


    @Override
    public RefsVO getRefs(int repositoryType, String project, String repo) {
        String repositoryUrl = acqRepositoryUrl(repositoryType, project, repo);
        try {
            RefsVO refsVO = getRefList(repositoryType, project, repo);
            RepositoryDO repositoryDO = jenkinsDao.queryRepositoryByUrl(repositoryUrl);
            coreLogger.info("git :{} ; refs : {}", repositoryUrl, repositoryDO);
            if (repositoryDO == null) {
                repositoryDO = new RepositoryDO(repositoryUrl, repositoryType);
                jenkinsDao.addRepository(repositoryDO);
            }
            saveRefs(refsVO, repositoryDO.getId());
            return refsVO;
        } catch (Exception e) {
            e.printStackTrace();
            return new RefsVO();
        }
    }

    @Override
    public RefsVO getRefsByUrl(String repositoryUrl, int repositoryType) {
        String project = acqProject(repositoryUrl);
        String repo = acqRepo(repositoryUrl);
        return getRefs(repositoryType, project, repo);
    }

    /**
     * 删除并保存所有的refs
     *
     * @param refsVO
     * @param repositoryId
     */
    private void saveRefs(RefsVO refsVO, long repositoryId) {
        List<RefsDO> list = jenkinsDao.queryRefsByRepoId(repositoryId);
        for (RefsDO ref : list) {
            try {
                jenkinsDao.delRefs(ref.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (String branches : refsVO.getBranches()) {
            RefsDO refs = new RefsDO(repositoryId, RefsDO.RefTypeEnum.branches.getCode(), branches);
            jenkinsDao.addRefs(refs);
        }
        for (String tags : refsVO.getTags()) {
            RefsDO refs = new RefsDO(repositoryId, RefsDO.RefTypeEnum.tags.getCode(), tags);
            jenkinsDao.addRefs(refs);
        }
    }


    /**
     * git@gitlab.51xianqu.net:xq_ios/lianhuarn.git
     *
     * @param repositoryType
     * @param project
     * @param repo
     * @return
     */
    @Override
    public String acqRepositoryGit(int repositoryType, String project, String repo) {
        HashMap<String, String> configMap = acqConifMap();
        String repositoryGit = "";
        if (repositoryType == GITLAB_REPOSITORY) {
            repositoryGit = configMap.get(JenkinsItemEnum.GITLAB_HOST.getItemKey());
            repositoryGit = repositoryGit.replace("http://", "git@");
            repositoryGit += ":" + project + "/" + repo + ".git";
        }
        // git@host:scm/xq_ios/lianhua.git
        if (repositoryType == STASH_REPOSITORY) {
            repositoryGit = configMap.get(JenkinsItemEnum.STASH_HOST.getItemKey());
            repositoryGit = repositoryGit.replace("http://", "git@");
            repositoryGit += ":scm/" + project + "/" + repo + ".git";
        }
        return repositoryGit;
    }

    @Override
    public String acqRepositoryGitByUrl(int repositoryType, String repositoryUrl) {
        String project = acqProject(repositoryUrl);
        String repo = acqRepo(repositoryUrl);
        return acqRepositoryUrl(repositoryType, project, repo);
    }


    private RefsVO acqRefsVO(List<RefsDO> refs) {
        List<String> branches = new ArrayList<String>();
        List<String> tags = new ArrayList<String>();
        for (RefsDO ref : refs) {
            if (ref.getRefType() == RefsDO.RefTypeEnum.branches.getCode()) {
                branches.add(ref.getRef());
                continue;
            }
            if (ref.getRefType() == RefsDO.RefTypeEnum.tags.getCode()) {
                tags.add(ref.getRef());
                continue;
            }
        }
        RefsVO refsVO = new RefsVO(branches, tags);
        return refsVO;
    }


    /**
     * http://host/scm/xq_ios/lianhua.git
     * git@gitlab.51xianqu.net:xq_ios/lianhuarn.git
     *
     * @param repositoryUrl
     * @return
     */
    @Override
    public RefsVO queryRefsByUrl(String repositoryUrl, int repositoryType) {
        String project = acqProject(repositoryUrl);
        String repo = acqRepo(repositoryUrl);
        return queryRefs(repositoryType, project, repo);
    }


    /**
     * 获取项目名称
     *
     * @param repositoryUrl
     * @return
     */
    @Override
    public String acqProject(String repositoryUrl) {
        try {
            String[] r = repositoryUrl.split(":");
            String[] p = r[1].split("/");
            return p[0];
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取仓库名称
     *
     * @param repositoryUrl
     * @return
     */
    @Override
    public String acqRepo(String repositoryUrl) {
        try {
            String[] r = repositoryUrl.split(":");
            String[] p = r[1].split("/");
            String repo = p[1];
            return repo.replace(".git", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public TableVO<List<StashProjectDO>> queryStashProjectPage(String name, int page, int length) {
        long size = stashDao.getStashProjectSize(name);
        List<StashProjectDO> stashProjectDOList = stashDao.getStashProjectPage(name, page * length, length);
        return new TableVO<>(size, stashProjectDOList);
    }

    @Override
    public StashProjectDO getStashProject(long id) {
        return stashDao.getStashProjectById(id);
    }

    @Override
    public TableVO<List<StashRepositoryDO>> queryStashRepositoryPage(long id, String name, int page, int length) {
        long size = stashDao.getStashRepositorySize(name, id);
        List<StashRepositoryDO> stashRepositoryDOList = stashDao.getStashRepositoryPage(name, id, page * length, length);
        return new TableVO<>(size, stashRepositoryDOList);
    }


    @Override
    public TableVO<List<ScmPermissionsVO>> queryScmPermissionsPage(String groupName, String scmProject, int page, int length) {
        long size = scmDao.getScmPermissionsSize(groupName, scmProject);
        List<ScmPermissionsDO> scmPermissionsDOList = scmDao.getScmPermissionsPage(groupName, scmProject, page * length, length);
        List<ScmPermissionsVO> voList = new ArrayList<>();
        for (ScmPermissionsDO scmPermissionsDO : scmPermissionsDOList) {
            ScmPermissionsVO scmPermissionsVO = new ScmPermissionsVO(scmPermissionsDO);
            if (scmPermissionsDO.getCiUserGroupId() > 0) {
                CiUserGroupDO ciUserGroupDO = userDao.getCiUserGroupById(scmPermissionsDO.getCiUserGroupId());
                scmPermissionsVO.setCiUserGroupDO(ciUserGroupDO);
            }
            voList.add(scmPermissionsVO);
        }

        return new TableVO<>(size, voList);
    }

    @Override
    public ScmPermissionsDO getScmPermissions(String scmProject) {
        ScmPermissionsDO scmPermissionsDO = scmDao.getScmPermissionsByScmProject(scmProject);
        return scmPermissionsDO;
    }


    @Override
    public BusinessWrapper<Boolean> scmPermissionsRefresh() {
        List<StashProjectDO> stashProjects = stashDao.getStashProjectAll();
        for (StashProjectDO stashProjectDO : stashProjects) {
            ScmPermissionsDO scmPermissionsDO = scmDao.queryScmPermissionsByScmProject(stashProjectDO.getName());
            // 若project不存在，则代表是新项目，需要生成对应的配置文件
            if (scmPermissionsDO == null) {
                scmPermissionsDO = new ScmPermissionsDO(stashProjectDO);
                try {
                    scmDao.addScmPermissions(scmPermissionsDO);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new BusinessWrapper<>(false);
                }
            }
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> savePermissions(ScmPermissionsVO scmPermissionsVO) {

        scmPermissionsVO.setCiUserGroupId(scmPermissionsVO.getCiUserGroupDO().getId());
        scmPermissionsVO.setGroupName(scmPermissionsVO.getCiUserGroupDO().getGroupName());
        try {
            scmDao.updateScmPermissions(scmPermissionsVO);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(false);
        }
        return new BusinessWrapper<>(true);
    }


}
