package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.facade.user.UserPermissionFacade;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/5/27 2:38 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserPermissionFacadeImpl implements UserPermissionFacade {

    private final UserPermissionService permissionService;

    private final AuthRoleService authRoleService;

    @Override
    public void revokeByUserId(int userId) {
        List<UserPermission> permissions = permissionService.queryByUserId(userId);
        if (CollectionUtils.isEmpty(permissions)) {
            return;
        }
        permissions.forEach(permissionService::delete);
    }

    @Override
    public void revokeUserBusinessPermission(UserBusinessPermissionParam.UserBusinessPermission userBusinessPermission) {
        permissionService.delete(BeanCopierUtil.copyProperties(userBusinessPermission, UserPermission.class));
    }

    @Override
    public void revokeUserPermissionByBusiness(int businessType, int businessId) {
        UserPermission userPermission = UserPermission.builder()
                .businessType(businessType)
                .businessId(businessId)
                .build();
        List<UserPermission> permissions = permissionService.queryByBusiness(userPermission);
        if (CollectionUtils.isEmpty(permissions)) {
            return;
        }
        permissions.forEach(e -> permissionService.deleteById(e.getId()));
    }

    @Override
    public void grantUserBusinessPermission(UserBusinessPermissionParam.UserBusinessPermission userBusinessPermission) {
        permissionService.add(BeanCopierUtil.copyProperties(userBusinessPermission, UserPermission.class));
    }

    @Override
    public void setUserBusinessPermission(int id) {
        UserPermission userPermission = permissionService.getById(id);
        userPermission.setPermissionRole(StringUtils.isEmpty(userPermission.getPermissionRole()) ? "admin" : "");
        permissionService.update(userPermission);
    }

    @Override
    public int getUserAccessLevel(String username) {
        if (StringUtils.isEmpty(username)) {
            return AccessLevel.ADMIN.getLevel();
        }
        return Optional.of(authRoleService.getRoleAccessLevelByUsername(username))
                .orElse(AccessLevel.DEF.getLevel());
    }

}