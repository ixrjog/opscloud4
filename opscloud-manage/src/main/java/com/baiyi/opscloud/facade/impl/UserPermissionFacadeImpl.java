package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserPermission;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;
import com.baiyi.opscloud.facade.UserPermissionFacade;
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

    @Override
    public void syncUserBusinessPermission(List<OcUserVO.User> userList, int businessType, int businessId) {
        try {
            for (OcUserVO.User user : userList) {
                OcUserPermission ocUserPermission = new OcUserPermission();
                ocUserPermission.setBusinessType(businessType);
                ocUserPermission.setBusinessId(businessId);
                ocUserPermission.setUserId(user.getId());
                addOcUserPermission(ocUserPermission);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void syncUserBusinessPermission(int userId, int businessType, List<Integer> businessIds) {
        try {
            for (Integer businessId : businessIds) {
                OcUserPermission ocUserPermission = new OcUserPermission();
                ocUserPermission.setBusinessType(businessType);
                ocUserPermission.setBusinessId(businessId);
                ocUserPermission.setUserId(userId);
                addOcUserPermission(ocUserPermission);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public BusinessWrapper<Boolean> addOcUserPermission(OcUserPermission ocUserPermission) {
        OcUserPermission checkOcUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission);
        if (checkOcUserPermission != null)
            return new BusinessWrapper<>(ErrorEnum.USER_PERMISSION_EXIST);
        ocUserPermissionService.addOcUserPermission(ocUserPermission);
        return BusinessWrapper.SUCCESS;
    }


    @Override
    public BusinessWrapper<Boolean> delOcUserPermission(OcUserPermission ocUserPermission) {
        OcUserPermission permission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission);
        if (permission != null)
            ocUserPermissionService.delOcUserPermissionById(permission.getId());
        return BusinessWrapper.SUCCESS;
    }

}
