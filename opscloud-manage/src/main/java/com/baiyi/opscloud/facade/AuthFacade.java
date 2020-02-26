package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.auth.GroupParam;
import com.baiyi.opscloud.domain.param.auth.ResourceParam;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import com.baiyi.opscloud.domain.param.auth.UserRoleParam;
import com.baiyi.opscloud.domain.vo.auth.*;

/**
 * @Author baiyi
 * @Date 2020/2/13 8:20 下午
 * @Version 1.0
 */
public interface AuthFacade {

    DataTable<OcRoleVO.Role> queryRolePage(RoleParam.PageQuery pageQuery);

    void addRole(OcRoleVO.Role role);

    void updateRole(OcRoleVO.Role role);

    BusinessWrapper<Boolean> deleteRoleById(int id);

   // DataTable<OcRoleResourceVO.OcRoleResource> queryRoleResourcePage(RoleResourceParam.PageQuery pageQuery);

    /**
     * 角色绑定的资源
     * @param pageQuery
     * @return
     */
    DataTable<OcResourceVO.Resource> queryRoleBindResourcePage(ResourceParam.BindResourcePageQuery pageQuery);

    DataTable<OcResourceVO.Resource> queryRoleUnbindResourcePage(ResourceParam.BindResourcePageQuery pageQuery);

    void bindRoleResource(OcRoleResourceVO.RoleResource roleResource);

    void unbindRoleResource(int roleResourceId);

    DataTable<OcResourceVO.Resource> queryResourcePage(ResourceParam.PageQuery pageQuery);

    void addResource(OcResourceVO.Resource resource);

    void updateResource(OcResourceVO.Resource resource);

    void updateResourceNeedAuth(OcResourceVO.Resource resource);

    BusinessWrapper<Boolean> deleteResourceById(int id);

    // resource group
    DataTable<OcGroupVO.Group> queryGroupPage(GroupParam.PageQuery pageQuery);

    void addGroup(OcGroupVO.Group group);

    void updateGroup(OcGroupVO.Group group);

    BusinessWrapper<Boolean> deleteGroupById(int id);

    DataTable<OcUserRoleVO.UserRole> queryUserRolePage(UserRoleParam.PageQuery pageQuery);

    void addUserRole(OcUserRoleVO.UserRole userRole);

    BusinessWrapper<Boolean> deleteUserRoleById(int id);

    /**
     * 公共接口2次鉴权
     * @param resourceName
     * @return
     */
    BusinessWrapper<Boolean> authenticationByResourceName(String resourceName);

}
