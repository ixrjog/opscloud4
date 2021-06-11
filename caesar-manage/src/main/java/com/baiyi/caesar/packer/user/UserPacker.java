package com.baiyi.caesar.packer.user;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.RegexUtil;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.packer.auth.AuthRolePacker;
import com.baiyi.caesar.packer.base.SecretPacker;
import com.baiyi.caesar.packer.desensitized.DesensitizedPacker;
import com.baiyi.caesar.util.ExtendUtil;
import org.apache.commons.lang3.StringUtils;
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
public class UserPacker extends SecretPacker {

    @Resource
    private AuthRolePacker authRolePacker;

    @Resource
    private UserCredentialPacker userCredentialPacker;

    @Resource
    private DesensitizedPacker<UserVO.User> desensitizedPacker;

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
        if (!StringUtils.isEmpty(pre.getPassword())) {
            RegexUtil.checkPasswordRule(pre.getPassword());
            pre.setPassword(encrypt(pre.getPassword()));
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
}
