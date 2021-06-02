package com.baiyi.caesar.packer.user;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.UserPermission;
import com.baiyi.caesar.service.user.UserPermissionService;
import com.baiyi.caesar.domain.vo.user.UserPermissionVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/27 4:12 下午
 * @Version 1.0
 */
@Component
public class UserPermissionPacker {

    @Resource
    private UserPermissionService permissionService;

    public void wrap(UserVO.IUserPermission iUserPermission) {
        UserPermission query = UserPermission.builder()
                .userId(iUserPermission.getUserId())
                .businessId(iUserPermission.getBusinessId())
                .businessType(iUserPermission.getBusinessType())
                .build();

        UserPermission userPermission = permissionService.getByUserPermission(query);
        if (userPermission != null)
            iUserPermission.setUserPermission(BeanCopierUtil.copyProperties(userPermission, UserPermissionVO.UserPermission.class));
    }

}
