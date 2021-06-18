package com.baiyi.caesar.packer.user;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.UserGroup;
import com.baiyi.caesar.domain.generator.caesar.UserPermission;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.domain.vo.user.UserGroupVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.service.user.UserPermissionService;
import com.baiyi.caesar.service.user.UserService;
import com.baiyi.caesar.util.ExtendUtil;
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
