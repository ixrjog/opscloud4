package com.baiyi.opscloud.jumpserver.builder;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.jumpserver.bo.UsersUserBO;
import com.baiyi.opscloud.jumpserver.center.impl.JumpserverCenterImpl;

/**
 * @Author baiyi
 * @Date 2020/3/8 5:55 下午
 * @Version 1.0
 */
public class UsersUserBuilder {

    public static UsersUser build(OcUser ocUser) {
        UsersUserBO usersUserBO = UsersUserBO.builder()
                .username(ocUser.getUsername())
                .name(ocUser.getDisplayName())
                .email(ocUser.getEmail())
                .phone(ocUser.getPhone()== null ? "" : ocUser.getPhone())
                .wechat(ocUser.getWechat() == null ? "" : ocUser.getWechat())
                .id(UUIDUtils.getUUID())
                .dateExpired(TimeUtils.gmtToDate(JumpserverCenterImpl.DATE_EXPIRED))
                .build();
        return covert(usersUserBO);
    }

    public static UsersUser build(OcUser ocUser,String role) {
        UsersUser usersUser =build(ocUser);
        usersUser.setRole(role);
        return usersUser;
    }

    private static UsersUser covert(UsersUserBO usersUserBO) {
        return BeanCopierUtils.copyProperties(usersUserBO, UsersUser.class);
    }
}
