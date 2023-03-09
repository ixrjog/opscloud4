package com.baiyi.opscloud.packer.business;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.vo.business.IBusinessPermissionUser;
import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/13 10:30 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class BusinessPermissionUserPacker {

    private final UserPermissionService userPermissionService;

    private final UserService userService;

    /**
     * 业务授权用户
     *
     * @param iBusinessPermissionUser
     */
    public void wrap(IBusinessPermissionUser iBusinessPermissionUser) {
        UserPermission userPermission = UserPermission.builder()
                .businessType(iBusinessPermissionUser.getBusinessType())
                .businessId(iBusinessPermissionUser.getBusinessId())
                .build();
        List<UserPermission> userPermissions = userPermissionService.queryByBusiness(userPermission);
        iBusinessPermissionUser.setUsers(
                userPermissions.stream()
                        .map(e -> {
                            UserVO.User vo = BeanCopierUtil.copyProperties(userService.getById(e.getUserId()), UserVO.User.class);
                            vo.setUserPermission(BeanCopierUtil.copyProperties(e, UserPermissionVO.UserPermission.class));
                            return vo;
                        })
                        .filter(UserVO.User::getIsActive)
                        .collect(Collectors.toList())
        );
    }

}
