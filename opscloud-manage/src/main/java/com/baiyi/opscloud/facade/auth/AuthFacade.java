package com.baiyi.opscloud.facade.auth;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.auth.*;
import com.baiyi.opscloud.domain.vo.auth.AuthGroupVO;
import com.baiyi.opscloud.domain.vo.auth.AuthResourceVO;
import com.baiyi.opscloud.domain.vo.auth.AuthRoleVO;


/**
 * @Author baiyi
 * @Date 2020/2/13 8:20 下午
 * @Version 1.0
 */
public interface AuthFacade {

    DataTable<AuthRoleVO.Role> queryRolePage(AuthRoleParam.AuthRolePageQuery pageQuery);

    void addRole(AuthRoleParam.Role role);

    void updateRole(AuthRoleParam.Role role);

    void deleteRoleById(int id);

    DataTable<AuthGroupVO.Group> queryGroupPage(AuthGroupParam.AuthGroupPageQuery pageQuery);

    void  addGroup(AuthGroupParam.Group group);

    void  updateGroup(AuthGroupParam.Group group);

    void deleteGroupById(int id);

    DataTable<AuthResourceVO.Resource> queryRoleBindResourcePage(AuthResourceParam.RoleBindResourcePageQuery pageQuery);

    void addRoleResource(AuthRoleResourceParam.RoleResource roleResource);

    void deleteRoleResourceById(int id);

    DataTable<AuthResourceVO.Resource> queryResourcePage(AuthResourceParam.AuthResourcePageQuery pageQuery);

    void addResource(AuthResourceParam.Resource resource);

    void updateResource(AuthResourceParam.Resource resource);

    void deleteResourceById(int id);

    void updateUserRole(AuthUserRoleParam.UpdateUserRole updateUserRole);

}
