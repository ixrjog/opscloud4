package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.decorator.ResourceDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.auth.GroupParam;
import com.baiyi.opscloud.domain.param.auth.ResourceParam;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import com.baiyi.opscloud.domain.param.auth.UserRoleParam;
import com.baiyi.opscloud.domain.vo.auth.*;
import com.baiyi.opscloud.facade.AuthFacade;
import com.baiyi.opscloud.service.auth.*;
import com.baiyi.opscloud.service.user.OcUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/2/13 8:21 下午
 * @Version 1.0
 */
@Service
public class AuthFacadeImpl implements AuthFacade {

    @Resource
    private OcAuthRoleService ocAuthRoleService;

    @Resource
    private OcAuthResourceService ocAuthResourceService;

    @Resource
    private OcAuthGroupService ocAuthGroupService;

    @Resource
    private OcAuthRoleResourceService ocAuthRoleResourceService;

    @Resource
    private OcAuthUserRoleService ocAuthUserRoleService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private ResourceDecorator resourceDecorator;

    @Override
    public DataTable<OcRoleVO.Role> queryRolePage(RoleParam.PageQuery pageQuery) {
        DataTable<OcAuthRole> table = ocAuthRoleService.queryOcAuthRoleByParam(pageQuery);
        List<OcRoleVO.Role> page = BeanCopierUtils.copyListProperties(table.getData(), OcRoleVO.Role.class);
        DataTable<OcRoleVO.Role> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public void addRole(OcRoleVO.Role role) {
        OcAuthRole ocAuthRole = BeanCopierUtils.copyProperties(role, OcAuthRole.class);
        ocAuthRoleService.addOcAuthRole(ocAuthRole);
    }

    @Override
    public void updateRole(OcRoleVO.Role role) {
        OcAuthRole ocAuthRole = BeanCopierUtils.copyProperties(role, OcAuthRole.class);
        ocAuthRoleService.updateOcAuthRole(ocAuthRole);
    }

    @Override
    public BusinessWrapper<Boolean> deleteRoleById(int id) {
        // 此处要判断是否有用户绑定role
        OcAuthRole ocAuthRole = ocAuthRoleService.queryOcAuthRoleById(id);
        if (ocAuthRole == null)
            return new BusinessWrapper<>(ErrorEnum.AUTH_ROLE_NOT_EXIST);
        int count = ocAuthUserRoleService.countByRoleId(id);
        if (count == 0) {
            ocAuthRoleService.deleteOcAuthRoleById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.AUTH_ROLE_HAS_USED);
        }
    }

    @Override
    public DataTable<OcResourceVO.Resource> queryRoleBindResourcePage(ResourceParam.BindResourcePageQuery pageQuery) {
        DataTable<OcAuthResource> table = ocAuthResourceService.queryRoleBindOcAuthResourceByParam(pageQuery);
        List<OcResourceVO.Resource> page = BeanCopierUtils.copyListProperties(table.getData(), OcResourceVO.Resource.class);
        DataTable<OcResourceVO.Resource> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public DataTable<OcResourceVO.Resource> queryRoleUnbindResourcePage(ResourceParam.BindResourcePageQuery pageQuery) {
        DataTable<OcAuthResource> table = ocAuthResourceService.queryRoleUnbindOcAuthResourceByParam(pageQuery);
        List<OcResourceVO.Resource> page = BeanCopierUtils.copyListProperties(table.getData(), OcResourceVO.Resource.class);
        DataTable<OcResourceVO.Resource> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public void bindRoleResource(OcRoleResourceVO.RoleResource roleResource) {
        OcAuthRoleResource ocAuthRoleResource = BeanCopierUtils.copyProperties(roleResource, OcAuthRoleResource.class);
        ocAuthRoleResourceService.addOcAuthRoleResource(ocAuthRoleResource);
    }

    @Override
    public void unbindRoleResource(int ocRoleResourceId) {
        ocAuthRoleResourceService.delOcAuthRoleResourceById(ocRoleResourceId);
    }

    @Override
    public DataTable<OcResourceVO.Resource> queryResourcePage(ResourceParam.PageQuery pageQuery) {
        DataTable<OcAuthResource> table = ocAuthResourceService.queryOcAuthResourceByParam(pageQuery);
        List<OcResourceVO.Resource> page = BeanCopierUtils.copyListProperties(table.getData(), OcResourceVO.Resource.class);
        DataTable<OcResourceVO.Resource> dataTable = new DataTable<>(page.stream().map(e -> resourceDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public void addResource(OcResourceVO.Resource resource) {
        OcAuthResource ocAuthResource = BeanCopierUtils.copyProperties(resource, OcAuthResource.class);
        ocAuthResourceService.addOcAuthResource(ocAuthResource);
    }

    @Override
    public void updateResource(OcResourceVO.Resource resource) {
        OcAuthResource ocAuthResource = BeanCopierUtils.copyProperties(resource, OcAuthResource.class);
        ocAuthResourceService.updateOcAuthResource(ocAuthResource);
    }

    @Override
    public void updateResourceNeedAuth(OcResourceVO.Resource resource) {
        OcAuthResource ocAuthResource = ocAuthResourceService.queryOcAuthResourceById(resource.getId());
        ocAuthResource.setNeedAuth(resource.getNeedAuth());
        ocAuthResourceService.updateOcAuthResource(ocAuthResource);
    }

    @Override
    public BusinessWrapper<Boolean> deleteResourceById(int id) {
        // 此处要判断是否有用户绑定role
        OcAuthResource ocAuthResource = ocAuthResourceService.queryOcAuthResourceById(id);
        if (ocAuthResource == null)
            return new BusinessWrapper<>(ErrorEnum.AUTH_RESOURCE_NOT_EXIST);
        // 判断role绑定的资源
        int count = ocAuthRoleResourceService.countByResourceId(id);
        if (count == 0) {
            ocAuthResourceService.deleteOcAuthResourceById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.AUTH_RESOURCE_HAS_USED);
        }
    }

    @Override
    public DataTable<OcGroupVO.Group> queryGroupPage(GroupParam.PageQuery pageQuery) {
        DataTable<OcAuthGroup> table = ocAuthGroupService.queryOcAuthGroupByParam(pageQuery);
        List<OcGroupVO.Group> page = BeanCopierUtils.copyListProperties(table.getData(), OcGroupVO.Group.class);
        DataTable<OcGroupVO.Group> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public void addGroup(OcGroupVO.Group group) {
        OcAuthGroup ocAuthGroup = BeanCopierUtils.copyProperties(group, OcAuthGroup.class);
        ocAuthGroupService.addOcAuthGroup(ocAuthGroup);
    }

    @Override
    public void updateGroup(OcGroupVO.Group group) {
        OcAuthGroup ocAuthGroup = BeanCopierUtils.copyProperties(group, OcAuthGroup.class);
        ocAuthGroupService.updateOcAuthGroup(ocAuthGroup);
    }

    @Override
    public BusinessWrapper<Boolean> deleteGroupById(int id) {
        // 此处要判断是否有用户绑定role
        OcAuthGroup ocAuthGroup = ocAuthGroupService.queryOcAuthGroupById(id);
        if (ocAuthGroup == null)
            return new BusinessWrapper<>(ErrorEnum.AUTH_GROUP_NOT_EXIST);
        int count = ocAuthResourceService.countByGroupId(id);
        if (count == 0) {
            ocAuthGroupService.deleteOcAuthGroupById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.AUTH_GROUP_HAS_USED);
        }
    }

    @Override
    public DataTable<OcUserRoleVO.UserRole> queryUserRolePage(UserRoleParam.PageQuery pageQuery) {
        DataTable<OcAuthUserRole> table = ocAuthUserRoleService.queryOcAuthUserRoleByParam(pageQuery);
        List<OcUserRoleVO.UserRole> page = BeanCopierUtils.copyListProperties(table.getData(), OcUserRoleVO.UserRole.class);
        DataTable<OcUserRoleVO.UserRole> dataTable = new DataTable<>(page.stream().map(e -> invokeOcUser(e)).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    /**
     * 插入用户信息
     *
     * @param userRole
     * @return
     */
    private OcUserRoleVO.UserRole invokeOcUser(OcUserRoleVO.UserRole userRole) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(userRole.getUsername());
        userRole.setDisplayName(ocUser.getDisplayName());
        OcAuthRole ocAuthRole = ocAuthRoleService.queryOcAuthRoleById(userRole.getRoleId());
        userRole.setRoleName(ocAuthRole.getRoleName());
        userRole.setRoleComment(ocAuthRole.getComment());
        return userRole;
    }

    @Override
    public void addUserRole(OcUserRoleVO.UserRole userRole) {
        try {
            OcAuthUserRole ocAuthUserRole = BeanCopierUtils.copyProperties(userRole, OcAuthUserRole.class);
            ocAuthUserRoleService.addOcAuthUserRole(ocAuthUserRole);
        } catch (Exception e) {
        }
    }

    @Override
    public BusinessWrapper<Boolean> deleteUserRoleById(int id) {
        OcAuthUserRole ocAuthUserRole = ocAuthUserRoleService.queryOcAuthUserRoleById(id);
        if (ocAuthUserRole == null)
            return new BusinessWrapper<>(ErrorEnum.AUTH_USER_ROLE_NOT_EXIST);
        ocAuthUserRoleService.deleteOcAuthUserRoleById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> authenticationByResourceName(String resourceName) {
        String username = SessionUtils.getUsername();
        if(ocAuthUserRoleService.authenticationByUsernameAndResourceName(username,resourceName))
            return BusinessWrapper.SUCCESS;
        return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
    }

}
