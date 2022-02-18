package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.ExtendUtil;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:29 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserGroupPacker implements IWrapper<UserGroupVO.UserGroup> {

    private final UserPermissionService userPermissionService;

    private final UserService userService;

    @Override
    public void wrap(UserGroupVO.UserGroup userGroup, IExtend iExtend) {
        if (!ExtendUtil.isExtend(iExtend)) {
            return;
        }
        UserPermission query = UserPermission.builder()
                .businessId(userGroup.getId())
                .businessType(BusinessTypeEnum.USERGROUP.getType())
                .build();
        List<UserPermission> userPermissions = userPermissionService.queryByBusiness(query);
        userGroup.setUserSize(userPermissions.size());
        userGroup.setUsers(
                userPermissions.stream().map(e ->
                        BeanCopierUtil.copyProperties(userService.getById(e.getUserId()), UserVO.User.class)
                ).collect(Collectors.toList())
        );
    }

}
