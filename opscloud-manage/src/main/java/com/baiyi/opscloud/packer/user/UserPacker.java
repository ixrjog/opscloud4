package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.RegexUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.packer.auth.AuthRolePacker;
import com.baiyi.opscloud.packer.desensitized.DesensitizedPacker;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.util.ExtendUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:49 上午
 * @Version 1.0
 */
@Component
public class UserPacker {

    @Resource
    private AuthRolePacker authRolePacker;

    @Resource
    private UserCredentialPacker userCredentialPacker;

    @Resource
    private DesensitizedPacker<UserVO.User> desensitizedPacker;

    @Resource
    private UserService userService;


    public List<UserVO.User> wrapVOList(List<User> data) {
        List<UserVO.User> userList = BeanCopierUtil.copyListProperties(data, UserVO.User.class);
        return userList.stream()
                .map(e -> desensitizedPacker.desensitized(e))
                .collect(Collectors.toList());
    }

    public List<UserVO.User> wrapVOList(List<User> data, IExtend iExtend) {
        List<UserVO.User> voList = wrapVOList(data);
        return voList.stream().peek(e -> {
            if (ExtendUtil.isExtend(iExtend)) {
                wrap(e);
            }
        }).collect(Collectors.toList());
    }

    public User toDO(UserVO.User user) {
        User pre = BeanCopierUtil.copyProperties(user, User.class);
        if (!StringUtils.isEmpty(pre.getPassword()))
            RegexUtil.checkPasswordRule(pre.getPassword());
        if (!RegexUtil.isPhone(user.getPhone()))
            pre.setPhone(Strings.EMPTY);
        if (StringUtils.isEmpty(user.getUuid())) {
            pre.setUuid(IdUtil.buildUUID());
        }
        return pre;
    }


    public UserVO.User wrap(User user) {
        return wrap(BeanCopierUtil.copyProperties(user, UserVO.User.class));
    }

    public UserVO.User wrap(UserVO.User user) {
        authRolePacker.wrap(user);
        userCredentialPacker.wrap(user);
        return desensitizedPacker.desensitized(user);
    }

    public void wrap(UserVO.IUser iUser) {
        User user = userService.getByUsername(iUser.getUsername());
        if (user != null)
            iUser.setUser(wrap(user));
    }
}
