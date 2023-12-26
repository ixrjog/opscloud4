package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.workorder.constants.UserGroupConstants;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractUserGroupPermissionExtendedAbstractUserPermission;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * VPN权限申请工单票据处理
 * @Author baiyi
 * @Date 2022/1/7 10:50 AM
 * @Version 1.0
 */
@Component
public class VpnTicketProcessor extends AbstractUserGroupPermissionExtendedAbstractUserPermission {

    @Override
    public Set<String> getGroupNames() {
        return Sets.newHashSet(UserGroupConstants.VPN_USERS.getRole());
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.VPN.name();
    }

}