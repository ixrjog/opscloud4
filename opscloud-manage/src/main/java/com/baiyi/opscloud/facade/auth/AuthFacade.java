package com.baiyi.opscloud.facade.auth;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.auth.AuthGroupParam;
import com.baiyi.opscloud.domain.param.auth.AuthResourceParam;
import com.baiyi.opscloud.domain.param.auth.AuthRoleParam;
import com.baiyi.opscloud.domain.param.auth.AuthUserRoleParam;
import com.baiyi.opscloud.domain.vo.auth.AuthGroupVO;
import com.baiyi.opscloud.domain.vo.auth.AuthResourceVO;
import com.baiyi.opscloud.domain.vo.auth.AuthRoleResourceVO;
import com.baiyi.opscloud.domain.vo.auth.AuthRoleVO;


/**
 * @Author baiyi
 * @Date 2020/2/13 8:20 下午
 * @Version 1.0
 */
public interface AuthFacade {

    DataTable<AuthRoleVO.Role> queryRolePage(AuthRoleParam.AuthRolePageQuery pageQuery);

    void addRole(AuthRoleVO.Role role);

    void updateRole(AuthRoleVO.Role role);

    void deleteRoleById(int id);

    DataTable<AuthGroupVO.Group> queryGroupPage(AuthGroupParam.AuthGroupPageQuery pageQuery);

    void  addGroup(AuthGroupVO.Group group);

    void  updateGroup(AuthGroupVO.Group group);

    void deleteGroupById(int id);

    DataTable<AuthResourceVO.Resource> queryRoleBindResourcePage(AuthResourceParam.RoleBindResourcePageQuery pageQuery);

    void addRoleResource(AuthRoleResourceVO.RoleResource roleResource);

    void deleteRoleResourceById(int id);

    DataTable<AuthResourceVO.Resource> queryResourcePage(AuthResourceParam.AuthResourcePageQuery pageQuery);

    void addResource(AuthResourceVO.Resource resource);

    void updateResource(AuthResourceVO.Resource resource);

    void deleteResourceById(int id);

    void updateUserRole(AuthUserRoleParam.UpdateUserRole updateUserRole);

}
