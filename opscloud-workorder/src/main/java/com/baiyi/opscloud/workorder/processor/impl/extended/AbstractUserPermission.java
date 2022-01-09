package com.baiyi.opscloud.workorder.processor.impl.extended;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

/**
 * 用户通用权限抽象类
 *
 * @Author baiyi
 * @Date 2022/1/7 10:16 AM
 * @Version 1.0
 */
public abstract class AbstractUserPermission<T> extends AbstractTicketProcessor<T> {

    @Resource
    private UserPermissionService userPermissionService;

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, T entry) throws TicketProcessException {
        User createUser = queryCreateUser(ticketEntry);
        UserPermission prePermission = UserPermission.builder()
                .userId(createUser.getId())
                .businessType(ticketEntry.getBusinessType())
                .businessId(ticketEntry.getBusinessId())
                .permissionRole(ticketEntry.getRole())
                .build();
        // 查询是否重复授权
        UserPermission userPermission = userPermissionService.getByUnqueKey(prePermission);
        if (userPermission == null) {
            userPermissionService.add(prePermission); // 授权
        } else {
            final String role = StringUtils.isEmpty(prePermission.getPermissionRole()) ? "" : prePermission.getPermissionRole();
            // 更新授权角色
            if (!role.equals(userPermission.getPermissionRole())) {
                userPermission.setPermissionRole(role);
                userPermissionService.update(userPermission);
            }
        }
    }

}