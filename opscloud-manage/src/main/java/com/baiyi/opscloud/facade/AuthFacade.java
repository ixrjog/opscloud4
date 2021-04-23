package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.auth.GroupParam;
import com.baiyi.opscloud.domain.param.auth.ResourceParam;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import com.baiyi.opscloud.domain.param.auth.UserRoleParam;
import com.baiyi.opscloud.domain.vo.auth.*;
import com.baiyi.opscloud.domain.vo.auth.menu.MenuVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/13 8:20 下午
 * @Version 1.0
 */
public interface AuthFacade {

    DataTable<RoleVO.Role> queryRolePage(RoleParam.PageQuery pageQuery);

    void addRole(RoleVO.Role role);

    void updateRole(RoleVO.Role role);

    void grantUserRole(OcUser user, String roleName);

    BusinessWrapper<Boolean> deleteRoleById(int id);

   // DataTable<OcRoleResourceVO.OcRoleResource> queryRoleResourcePage(RoleResourceParam.PageQuery pageQuery);

    /**
     * 角色绑定的资源
     * @param pageQuery
     * @return
     */
    DataTable<ResourceVO.Resource> queryRoleBindResourcePage(ResourceParam.BindResourcePageQuery pageQuery);

    DataTable<ResourceVO.Resource> queryRoleUnbindResourcePage(ResourceParam.BindResourcePageQuery pageQuery);

    void bindRoleResource(RoleResourceVO.RoleResource roleResource);

    void unbindRoleResource(int roleResourceId);

    DataTable<ResourceVO.Resource> queryResourcePage(ResourceParam.PageQuery pageQuery);

    void addResource(ResourceVO.Resource resource);

    void updateResource(ResourceVO.Resource resource);

    void updateResourceNeedAuth(ResourceVO.Resource resource);

    BusinessWrapper<Boolean> deleteResourceById(int id);

    // resource group
    DataTable<GroupVO.Group> queryGroupPage(GroupParam.PageQuery pageQuery);

    void addGroup(GroupVO.Group group);

    void updateGroup(GroupVO.Group group);

    BusinessWrapper<Boolean> deleteGroupById(int id);

    DataTable<UserRoleVO.UserRole> queryUserRolePage(UserRoleParam.UserRolePageQuery pageQuery);

    List<UserRoleVO.UserRole> queryUserRoles(UserRoleParam.UserRolesQuery query);

    void addUserRole(UserRoleVO.UserRole userRole);

    BusinessWrapper<Boolean> deleteUserRoleById(int id);

    /**
     * 公共接口2次鉴权
     * @param resourceName
     * @return
     */
    BusinessWrapper<Boolean> authenticationByResourceName(String resourceName);

    List<MenuVO> queryUserMenu();

    BusinessWrapper<Boolean> saveRoleMenu(AuthMenuVO.Menu menu);

    AuthMenuVO.Menu queryRoleMenuByRoleId(int roleId);
}
