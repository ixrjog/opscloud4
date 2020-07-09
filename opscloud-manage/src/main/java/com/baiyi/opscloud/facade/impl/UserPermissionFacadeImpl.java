package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserPermission;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.UserPermissionFacade;
import com.baiyi.opscloud.service.auth.OcAuthRoleService;
import com.baiyi.opscloud.service.user.OcUserPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/25 7:11 下午
 * @Version 1.0
 */
@Service
public class UserPermissionFacadeImpl implements UserPermissionFacade {

    @Resource
    private OcUserPermissionService ocUserPermissionService;

    @Resource
    private OcAuthRoleService ocAuthRoleService;

    @Override
    public void syncUserBusinessPermission(List<UserVO.User> userList, int businessType, int businessId) {
        userList.forEach(e -> {
            try {
                OcUserPermission ocUserPermission = new OcUserPermission();
                ocUserPermission.setBusinessType(businessType);
                ocUserPermission.setBusinessId(businessId);
                ocUserPermission.setUserId(e.getId());
                addOcUserPermission(ocUserPermission);
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void syncUserBusinessPermission(int userId, int businessType, List<Integer> businessIds) {
        try {
            businessIds.forEach(e -> {
                try {
                    OcUserPermission ocUserPermission = new OcUserPermission();
                    ocUserPermission.setBusinessType(businessType);
                    ocUserPermission.setBusinessId(e);
                    ocUserPermission.setUserId(userId);
                    addOcUserPermission(ocUserPermission);
                } catch (Exception ignored) {
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public BusinessWrapper<Boolean> addOcUserPermission(OcUserPermission ocUserPermission) {
        OcUserPermission checkOcUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission);
        if (checkOcUserPermission == null)
            ocUserPermissionService.addOcUserPermission(ocUserPermission);
        return BusinessWrapper.SUCCESS;
    }


    @Override
    public BusinessWrapper<Boolean> delOcUserPermission(OcUserPermission ocUserPermission) {
        // 删除服务器组授权
        OcUserPermission permission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission);
        if (permission != null)
            ocUserPermissionService.delOcUserPermissionById(permission.getId());
        // 删除高权限
        ocUserPermission.setBusinessType(BusinessType.SERVER_ADMINISTRATOR_ACCOUNT.getType());
        OcUserPermission permissionAdminAccout = ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission);
        if (permissionAdminAccout != null)
            ocUserPermissionService.delOcUserPermissionById(permissionAdminAccout.getId());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public int getUserAccessLevel(OcUser ocUser) {
        return ocAuthRoleService.queryOcAuthRoleAccessLevelByUsername(ocUser.getUsername());
    }

    @Override
    public BusinessWrapper<Boolean> checkAccessLevel(OcUser ocUser, int accessLevel) {
        if (ocUser == null)
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        if (getUserAccessLevel(ocUser) >= accessLevel) {
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        }
    }

    @Override
    public boolean tryUserBusinessPermission(int userId, int businessType, int businessId) {
        OcUserPermission ocUserPermission = new OcUserPermission();
        ocUserPermission.setBusinessType(businessType);
        ocUserPermission.setBusinessId(businessId);
        ocUserPermission.setUserId(userId);
        return ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission) != null;
    }

}
