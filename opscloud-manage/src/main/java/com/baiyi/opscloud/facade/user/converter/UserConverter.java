package com.baiyi.opscloud.facade.user.converter;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.PasswordUtil;
import com.baiyi.opscloud.common.util.RegexUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.user.UserParam;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2022/2/28 1:24 PM
 * @Version 1.0
 */
public class UserConverter {

    private UserConverter() {
    }

    public static User toDO(UserParam.CreateUser createUser) {
        RegexUtil.isUsernameRule(createUser.getUsername());
        User user = BeanCopierUtil.copyProperties(createUser, User.class);
        if (StringUtils.isNotEmpty(user.getPassword())) {
            RegexUtil.checkPasswordRule(user.getPassword());
        } else {
            user.setPassword(PasswordUtil.generatorPassword(20, true));
        }
        if (!RegexUtil.isPhone(createUser.getPhone()))
            user.setPhone(StringUtils.EMPTY);
        if (StringUtils.isEmpty(createUser.getUuid()))
            user.setUuid(IdUtil.buildUUID());
        user.setMfa(false);
        user.setForceMfa(false);
        return user;
    }

    public static User toDO(UserParam.UpdateUser updateUser) {
        User pre = BeanCopierUtil.copyProperties(updateUser, User.class);
        if (!StringUtils.isEmpty(pre.getPassword()))
            RegexUtil.checkPasswordRule(pre.getPassword());
        if (!RegexUtil.isPhone(updateUser.getPhone()))
            pre.setPhone(StringUtils.EMPTY);
        if (StringUtils.isEmpty(updateUser.getUuid())) {
            pre.setUuid(IdUtil.buildUUID());
        }
        return pre;
    }

}
