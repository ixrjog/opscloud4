package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.workorder.constants.UserGroupConstants;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.processor.impl.base.permission.UserGroupExtendedUserPermission;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * CONFLUENCE权限申请工单票据处理
 * @Author baiyi
 * @Date 2022/1/7 1:55 PM
 * @Version 1.0
 */
@Component
public class ConfluenceTicketProcessor extends UserGroupExtendedUserPermission {

    @Override
    protected Set<String> getGroupNames() {
        return Sets.newHashSet(
                UserGroupConstants.CONFLUENCE_USERS.getRole()
        );
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.CONFLUENCE.name();
    }

}