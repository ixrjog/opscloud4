package com.baiyi.caesar.facade.auth.impl;


import com.baiyi.caesar.common.exception.auth.AuthRuntimeException;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.auth.AuthGroupParam;
import com.baiyi.caesar.domain.param.auth.AuthResourceParam;
import com.baiyi.caesar.domain.param.auth.AuthRoleParam;
import com.baiyi.caesar.domain.param.auth.AuthUserRoleParam;
import com.baiyi.caesar.facade.auth.AuthFacade;
import com.baiyi.caesar.packer.auth.AuthGroupPacker;
import com.baiyi.caesar.packer.auth.AuthResourcePacker;
import com.baiyi.caesar.packer.auth.AuthRolePacker;
import com.baiyi.caesar.service.auth.*;
import com.baiyi.caesar.vo.auth.AuthGroupVO;
import com.baiyi.caesar.vo.auth.AuthResourceVO;
import com.baiyi.caesar.vo.auth.AuthRoleResourceVO;
import com.baiyi.caesar.vo.auth.AuthRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;


/**
 * @Author baiyi
 * @Date 2020/2/13 8:21 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class AuthFacadeImpl implements AuthFacade {

    @Resource
    private AuthRoleService authRoleService;

    @Resource
    private AuthRolePacker authRolePacker;

    @Resource
    private AuthGroupService authGroupService;

    @Resource
    private AuthResourceService authResourceService;

    @Resource
    private AuthResourcePacker authResourcePacker;

    @Resource
    private AuthRoleResourceService authRoleResourceService;

    @Resource
    private AuthGroupPacker authGroupPacker;

    @Resource
    private AuthUserRoleService authUserRoleService;

    @Override
    public DataTable<AuthRoleVO.Role> queryRolePage(AuthRoleParam.AuthRolePageQuery pageQuery) {
        DataTable<AuthRole> table = authRoleService.queryPageByParam(pageQuery);
        return new DataTable<>(authRolePacker.wrapVOList(table.getData()), table.getTotalNum());
    }

    @Override
    public void addRole(AuthRoleVO.Role role) {
        authRoleService.add(BeanCopierUtil.copyProperties(role, AuthRole.class));
    }

    @Override
    public void updateRole(AuthRoleVO.Role role) {
        authRoleService.update(BeanCopierUtil.copyProperties(role, AuthRole.class));
    }

    @Override
    public void deleteRoleById(int id) {
        if (authRoleResourceService.countByRoleId(id) != 0)
            throw new AuthRuntimeException(ErrorEnum.AUTH_ROLE_HAS_USED);
        // TODO 暂时不删除 authRoleService.deleteById(id);
    }

    @Override
    public DataTable<AuthGroupVO.Group> queryGroupPage(AuthGroupParam.AuthGroupPageQuery pageQuery) {
        DataTable<AuthGroup> table = authGroupService.queryPageByParam(pageQuery);
        return new DataTable<>(authGroupPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public void addGroup(AuthGroupVO.Group group) {
        authGroupService.add(BeanCopierUtil.copyProperties(group, AuthGroup.class));
    }

    @Override
    public void updateGroup(AuthGroupVO.Group group) {
        authGroupService.update(BeanCopierUtil.copyProperties(group, AuthGroup.class));
    }

    @Override
    public void deleteGroupById(int id) {
        if (authResourceService.countByGroupId(id) != 0)
            throw new AuthRuntimeException(ErrorEnum.AUTH_GROUP_HAS_USED);
        // TODO 暂时不删除 authGroupService.deleteById(id);
    }

    @Override
    public DataTable<AuthResourceVO.Resource> queryRoleBindResourcePage(AuthResourceParam.RoleBindResourcePageQuery pageQuery) {
        DataTable<AuthResource> table = authResourceService.queryRoleBindResourcePageByParam(pageQuery);
        return new DataTable<>(authResourcePacker.wrapVOList(table.getData()), table.getTotalNum());
    }

    @Override
    public void addRoleResource(AuthRoleResourceVO.RoleResource roleResource) {
        try {
            authRoleResourceService.add(BeanCopierUtil.copyProperties(roleResource, AuthRoleResource.class));
        } catch (DuplicateKeyException ignored) {
        }
    }

    @Override
    public void deleteRoleResourceById(int id) {
        authRoleResourceService.deleteById(id);
    }

    @Override
    public DataTable<AuthResourceVO.Resource> queryResourcePage(AuthResourceParam.AuthResourcePageQuery pageQuery) {
        DataTable<AuthResource> table = authResourceService.queryPageByParam(pageQuery);
        return new DataTable<>(authResourcePacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public void updateResource(AuthResourceVO.Resource resource) {
        authResourceService.update(BeanCopierUtil.copyProperties(resource, AuthResource.class));
    }

    @Override
    public void addResource(AuthResourceVO.Resource resource) {
        authResourceService.add(BeanCopierUtil.copyProperties(resource, AuthResource.class));
    }

    @Override
    public void deleteResourceById(int id) {
        if (authRoleResourceService.countByResourceId(id) != 0)
            throw new AuthRuntimeException(ErrorEnum.AUTH_RESOURCE_HAS_USED);
        // TODO 暂时不删除 authResourceService.deleteById(id);
    }

    @Override
    public void updateUserRole(AuthUserRoleParam.UpdateUserRole updateUserRole) {
        List<AuthUserRole> roles = authUserRoleService.queryByUsername(updateUserRole.getUsername());
        updateUserRole.getRoleIds().forEach(id -> {
            if (checkAddUserRole(updateUserRole.getUsername(), roles, id)) {
                AuthUserRole pre = new AuthUserRole();
                pre.setUsername(updateUserRole.getUsername());
                pre.setRoleId(id);
                authUserRoleService.add(pre);
            }
        });
        roles.forEach(e -> authUserRoleService.deleteById(e.getId()));
    }

    private boolean checkAddUserRole(String username, List<AuthUserRole> roles, Integer roleId) {
        Iterator<AuthUserRole> iter = roles.iterator();
        while (iter.hasNext()) {
            AuthUserRole authUserRole = iter.next();
            if (username.equals(authUserRole.getUsername()) && roleId.equals(authUserRole.getRoleId())) {
                iter.remove();
                return false;
            }
        }
        return true;
    }

}
