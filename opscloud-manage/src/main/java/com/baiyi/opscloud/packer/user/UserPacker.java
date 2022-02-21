package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.packer.desensitized.DesensitizedPacker;
import com.baiyi.opscloud.packer.user.delegate.UserPackerDelegate;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:49 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserPacker implements IWrapper<UserVO.User> {

    private final DesensitizedPacker<UserVO.User> desensitizedPacker;

    private final UserService userService;

    private final UserPackerDelegate userPackerDelegate;

    private final UserAvatarPacker userAvatarPacker;

    private final UserPermissionPacker userPermissionPacker;

    @Override
    public void wrap(UserVO.User user, IExtend iExtend) {
        userPackerDelegate.wrap(user, iExtend);
        userPermissionPacker.wrap(user, iExtend);
        // 插入头像
        userAvatarPacker.wrap(user, iExtend);
        // 数据脱敏
        desensitizedPacker.desensitized(user);
    }

    public void wrap(UserVO.IUser iUser) {
        User user = userService.getByUsername(iUser.getUsername());
        if (user != null) {
            UserVO.User userVO = BeanCopierUtil.copyProperties(user, UserVO.User.class);
            wrap(userVO, SimpleExtend.EXTEND);
            iUser.setUser(userVO);
        }
    }

}
