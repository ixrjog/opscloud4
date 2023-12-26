package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractUserPermissionExtendedBaseTicketProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 服务器（组）权限申请工单票据处理
 *
 * @Author baiyi
 * @Date 2022/1/6 7:02 PM
 * @Version 1.0
 */
@Component
public class ServerGroupTicketProcessor extends AbstractUserPermissionExtendedBaseTicketProcessor<ServerGroup> {

    @Resource
    private ServerGroupService serverGroupService;

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SERVER_GROUP.name();
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        ServerGroup entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isEmpty(entry.getName())) {
            throw new TicketVerifyException("校验工单条目失败: 未指定服务器组名称！");
        }
        ServerGroup serverGroup = serverGroupService.getByName(entry.getName());
        if (serverGroup == null) {
            throw new TicketVerifyException("校验工单条目失败: 服务器组不存在！");
        }
        // 验证接口 IAllowOrder
        verifyEntry(serverGroup);
    }

    @Override
    public void update(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        handleUpdate(ticketEntry);
    }

    @Override
    protected Class<ServerGroup> getEntryClassT() {
        return ServerGroup.class;
    }

}