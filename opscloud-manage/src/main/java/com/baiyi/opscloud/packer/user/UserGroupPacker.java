package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.util.ExtendUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:29 下午
 * @Version 1.0
 */
@Component
public class UserGroupPacker {

    @Resource
    private UserPermissionService userPermissionService;

    @Resource
    private UserService userService;

    public List<UserGroupVO.UserGroup> wrapVOList(List<UserGroup> data, IExtend iExtend) {
        List<UserGroupVO.UserGroup> voList = BeanCopierUtil.copyListProperties(data, UserGroupVO.UserGroup.class);
        return voList.stream().peek(e -> {
            if (ExtendUtil.isExtend(iExtend)) {
                wrap(e);
            }
        }).collect(Collectors.toList());
    }

    private void wrap(UserGroupVO.UserGroup userGroup) {
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
