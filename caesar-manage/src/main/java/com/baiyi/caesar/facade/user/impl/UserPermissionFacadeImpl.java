package com.baiyi.caesar.facade.user.impl;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.UserPermission;
import com.baiyi.caesar.facade.user.UserPermissionFacade;
import com.baiyi.caesar.service.auth.AuthRoleService;
import com.baiyi.caesar.service.user.UserPermissionService;
import com.baiyi.caesar.domain.vo.user.UserPermissionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/27 2:38 下午
 * @Version 1.0
 */
@Service
public class UserPermissionFacadeImpl implements UserPermissionFacade {

    @Resource
    private UserPermissionService permissionService;

    @Resource
    private AuthRoleService authRoleService;

    @Override
    public void revokeUserBusinessPermission(UserPermissionVO.UserBusinessPermission userBusinessPermission) {
        permissionService.deleteByUserPermission(BeanCopierUtil.copyProperties(userBusinessPermission, UserPermission.class));
    }

    @Override
    public void grantUserBusinessPermission(UserPermissionVO.UserBusinessPermission userBusinessPermission) {
        permissionService.add(BeanCopierUtil.copyProperties(userBusinessPermission, UserPermission.class));
    }

    @Override
    public void settUserBusinessPermission(int id) {
        UserPermission userPermission = permissionService.getById(id);
        userPermission.setPermissionRole(StringUtils.isEmpty(userPermission.getPermissionRole()) ? "admin" : "");
        permissionService.update(userPermission);
    }

    @Override
    public int getUserAccessLevel(String username) {
        return authRoleService.getRoleAccessLevelByUsername(username);
    }

}
