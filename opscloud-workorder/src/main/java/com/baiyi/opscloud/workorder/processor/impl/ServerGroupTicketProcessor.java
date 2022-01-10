package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractUserPermissionExtendedBaseTicketProcessor;
import org.springframework.stereotype.Component;

/**
 * 服务器（组）权限申请工单票据处理
 * @Author baiyi
 * @Date 2022/1/6 7:02 PM
 * @Version 1.0
 */
@Component
public class ServerGroupTicketProcessor extends AbstractUserPermissionExtendedBaseTicketProcessor<ServerGroup> {

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SERVER_GROUP.name();
    }

    @Override
    protected Class<ServerGroup> getEntryClassT() {
        return ServerGroup.class;
    }

}
