package com.baiyi.caesar.packer.user;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.RegexUtil;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.packer.auth.AuthRolePacker;
import com.baiyi.caesar.packer.base.SecretParcker;
import com.baiyi.caesar.util.ExtendUtil;
import com.baiyi.caesar.domain.vo.user.UserVO;
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
public class UserPacker extends SecretParcker {

    @Resource
    private AuthRolePacker authRolePacker;

    public List<UserVO.User> wrapVOList(List<User> data) {
        return BeanCopierUtil.copyListProperties(data, UserVO.User.class);
    }

    public List<UserVO.User> wrapVOList(List<User> data, IExtend iExtend) {
        List<UserVO.User> voList = wrapVOList(data);
        return voList.stream().peek(e -> {
            e.setPassword("");
            if (ExtendUtil.isExtend(iExtend)) {
                authRolePacker.wrap(e);
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


}
