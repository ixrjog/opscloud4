package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.workorder.constants.UserGroupConstants;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractUserGroupPermissionExtendedAbstractUserPermission;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2022/3/22 10:15
 * @Version 1.0
 */
@Component
public class GrafanaTicketProcessor extends AbstractUserGroupPermissionExtendedAbstractUserPermission {

    @Override
    public Set<String> getGroupNames() {
        return Sets.newHashSet(
                UserGroupConstants.GRAFANA_ADMIN.getRole(),
                UserGroupConstants.GRAFANA_USERS.getRole(),
                UserGroupConstants.GRAFANA_EDITOR_USERS.getRole()
        );
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.GRAFANA.name();
    }

}