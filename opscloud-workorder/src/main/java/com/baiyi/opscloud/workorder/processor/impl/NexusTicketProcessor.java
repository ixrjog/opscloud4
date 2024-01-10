package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.workorder.constants.UserGroupConstants;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractUserGroupPermissionExtendedAbstractUserPermission;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * NEXUS权限申请工单票据处理
 * @Author baiyi
 * @Date 2022/1/7 11:04 AM
 * @Version 1.0
 */
@Component
public class NexusTicketProcessor extends AbstractUserGroupPermissionExtendedAbstractUserPermission {

    @Override
    public Set<String> getGroupNames() {
        return Sets.newHashSet(
                UserGroupConstants.NEXUS_USERS.getRole(),
                UserGroupConstants.NEXUS_DEVELOPER.getRole()
        );
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.NEXUS.name();
    }

}