package com.baiyi.opscloud.workorder.processor.impl.extended;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.processor.impl.base.BaseTicketProcessor;
import org.apache.commons.lang3.StringUtils;

import jakarta.annotation.Resource;

/**
 * 用户通用权限抽象类
 *
 * @Author baiyi
 * @Date 2022/1/7 10:16 AM
 * @Version 1.0
 */
public abstract class AbstractUserPermissionExtendedBaseTicketProcessor<T> extends BaseTicketProcessor<T> {

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
        UserPermission userPermission = userPermissionService.getByUniqueKey(prePermission);
        if (userPermission == null) {
            // 授权
            userPermissionService.add(prePermission);
        } else {
            final String role = StringUtils.isEmpty(prePermission.getPermissionRole()) ? "" : prePermission.getPermissionRole();
            // 更新授权角色
            if (!role.equals(userPermission.getPermissionRole())) {
                userPermission.setPermissionRole(role);
                userPermissionService.update(userPermission);
            }
        }
    }

    /**
     * 更新工单条目配置(修改角色)
     *
     * @param ticketEntry
     */
    protected void handleUpdate(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        WorkOrderTicket ticket = ticketService.getById(ticketEntry.getWorkOrderTicketId());
        if (!OrderTicketPhaseCodeConstants.NEW.name().equals(ticket.getTicketPhase())) {
            throw new TicketProcessException("工单进度不是新建，无法更新配置条目！");
        }
        String role = ticketEntry.getRole();
        WorkOrderTicketEntry preTicketEntry = ticketEntryService.getById(ticketEntry.getId());
        preTicketEntry.setRole(role);
        updateTicketEntry(preTicketEntry);
    }

}