package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.workorder.constants.UserGroupConstants;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractUserGroupPermissionExtendedAbstractUserPermission;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2023/3/11 14:36
 * @Version 1.0
 */
@Component
public class ApolloTicketProcessor extends AbstractUserGroupPermissionExtendedAbstractUserPermission {

    @Override
    public Set<String> getGroupNames() {
        return Sets.newHashSet(
                UserGroupConstants.APOLLO_USERS.getRole()
        );
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APOLLO.name();
    }

}