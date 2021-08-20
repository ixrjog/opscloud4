package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.facade.user.UserPermissionFacade;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

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
        permissionService.delete(BeanCopierUtil.copyProperties(userBusinessPermission, UserPermission.class));
    }

    @Override
    public void revokeUserPermissionByBusiness(int businessType, int businessId) {
        UserPermission userPermission = UserPermission.builder()
                .businessType(businessType)
                .businessId(businessId)
                .build();
        List<UserPermission> permissions = permissionService.queryByBusiness(userPermission);
        if(CollectionUtils.isEmpty(permissions)) return;
        permissions.forEach(e -> permissionService.deleteById(e.getId()));
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
