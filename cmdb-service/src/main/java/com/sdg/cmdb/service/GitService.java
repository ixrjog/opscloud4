package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.gitlab.RefsVO;
import com.sdg.cmdb.domain.scm.ScmPermissionsDO;
import com.sdg.cmdb.domain.scm.ScmPermissionsVO;
import com.sdg.cmdb.domain.todo.todoProperty.StashProjectDO;
import com.sdg.cmdb.domain.todo.todoProperty.StashRepositoryDO;

import java.util.List;


public interface GitService {


    RefsVO getRefList(int repositoryType, String project, String repo) throws Exception;


    /**
     * 缓存
     *
     * @param repositoryType
     * @param project
     * @param repo
     * @return
     */
    RefsVO queryRefs(int repositoryType, String project, String repo);

    /**
     * 查询分支（缓存）
     *
     * @param repositoryUrl
     * @param repositoryType
     * @return
     */
    RefsVO queryRefsByUrl(String repositoryUrl, int repositoryType);

    /**
     * 直接查询非缓存
     *
     * @param repositoryType
     * @param project
     * @param repo
     * @return
     */
    RefsVO getRefs(int repositoryType, String project, String repo);

    /**
     * 查询分支（非缓存）
     *
     * @param repositoryUrl
     * @param repositoryType
     * @return
     */
    RefsVO getRefsByUrl(String repositoryUrl, int repositoryType);

    String acqRepositoryGit(int repositoryType, String project, String repo);

    String acqRepositoryGitByUrl(int repositoryType,String repositoryUrl);

    String acqProject(String repositoryUrl);

    String acqRepo(String repositoryUrl);


    TableVO<List<StashProjectDO>> queryStashProjectPage(String name, int page, int length);

    StashProjectDO getStashProject(long id);

    TableVO<List<StashRepositoryDO>> queryStashRepositoryPage(long id, String name, int page, int length);


    TableVO<List<ScmPermissionsVO>> queryScmPermissionsPage(String groupName, String scmProject, int page, int length);

    ScmPermissionsDO getScmPermissions( String scmProject);

    /**
     * 刷新SCM项目配置
     * @return
     */
    BusinessWrapper<Boolean> scmPermissionsRefresh();

    BusinessWrapper<Boolean> savePermissions(ScmPermissionsVO scmPermissionsVO);

}
