package com.sdg.cmdb.dao.cmdb;


import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;
import com.sdg.cmdb.domain.scm.ScmPermissionsDO;
import com.sdg.cmdb.domain.server.ServerDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ScmDao {

    /**
     * 获取指定条件的scm权限的数目
     *
     * @param groupName
     * @param scmProject
     * @return
     */
    long getScmPermissionsSize(@Param("groupName") String groupName,
                               @Param("scmProject") String scmProject);

    /**
     * 获取指定条件的scm权限分页数据
     *
     * @param groupName
     * @param scmProject
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ScmPermissionsDO> getScmPermissionsPage(
            @Param("groupName") String groupName,
            @Param("scmProject") String scmProject,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    ScmPermissionsDO getScmPermissionsByScmProject(@Param("scmProject") String scmProject
    );


    ScmPermissionsDO queryScmPermissionsByScmProject(@Param("scmProject") String scmProject);

    /**
     * 新增ScmPermissions
     *
     * @param scmPermissionsDO
     * @return
     */
    int addScmPermissions(ScmPermissionsDO scmPermissionsDO);

    /**
     * 更新ScmPermissions
     *
     * @param scmPermissionsDO
     * @return
     */
    int updateScmPermissions(ScmPermissionsDO scmPermissionsDO);

    /**
     * 删除ScmPermissions
     *
     * @param id
     * @return
     */
    int delScmPermissionsById(@Param("id") long id);

}
