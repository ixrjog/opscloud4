package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.base.BaseTicketProcessor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 员工离职-工单处理逻辑
 *
 * @Author baiyi
 * @Date 2022/3/2 11:48 AM
 * @Version 1.0
 */
@Component
public class EmployeeResignTicketProcessor extends BaseTicketProcessor<User> {

    @Resource
    private UserService userService;

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, User entry) throws TicketProcessException {
        User user = userService.getById(ticketEntry.getBusinessId());
        if (user == null || !user.getIsActive()) {
            return;
        }
        userService.setInactive(user);
    }

    @Override
    protected Class<User> getEntryClassT() {
        return User.class;
    }

    @Override
    protected void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        // 判断是否有效用户
        User entry = this.toEntry(ticketEntry.getContent());
        User originalUser = userService.getById(ticketEntry.getBusinessId());
        if (!originalUser.getUsername().equals(entry.getUsername())) {
            throw new TicketVerifyException("校验工单条目失败: 离职员工不匹配！");
        }
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SYS_EMPLOYEE_RESIGN.name();
    }

}