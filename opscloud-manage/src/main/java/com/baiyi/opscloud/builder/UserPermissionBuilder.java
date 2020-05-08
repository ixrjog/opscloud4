package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.bo.UserPermissionBO;
import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserPermission;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
import com.baiyi.opscloud.factory.ticket.entry.ServerGroupEntry;

/**
 * @Author baiyi
 * @Date 2020/3/6 11:33 上午
 * @Version 1.0
 */
public class UserPermissionBuilder {

    public static OcUserPermission build(ServerGroupParam.UserServerGroupPermission userServerGroupPermission) {
        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(userServerGroupPermission.getUserId())
                .businessId(userServerGroupPermission.getServerGroupId())
                .businessType(BusinessType.SERVERGROUP.getType())
                .build();
        return covert(userPermissionBO);
    }

    public static OcUserPermission build(UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission) {
        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(userUserGroupPermission.getUserId())
                .businessId(userUserGroupPermission.getUserGroupId())
                .businessType(BusinessType.USERGROUP.getType())
                .build();
        return covert(userPermissionBO);
    }

    public static OcUserPermission build(OcUser ocUser, ServerGroupEntry serverGroupEntry) {
        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(ocUser.getId())
                .businessId(serverGroupEntry.getServerGroup().getId())
                .businessType(BusinessType.SERVER_ADMINISTRATOR_ACCOUNT.getType())
                .build();
        return covert(userPermissionBO);
    }

    private static OcUserPermission covert(UserPermissionBO userPermissionBO) {
        return BeanCopierUtils.copyProperties(userPermissionBO, OcUserPermission.class);
    }

}
