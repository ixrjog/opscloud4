package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.processor.impl.base.permission.UserPermissionExtendedAbstractTicketProcessor;
import org.springframework.stereotype.Component;

/**
 * 应用权限申请工单票据处理
 * @Author baiyi
 * @Date 2022/1/7 10:26 AM
 * @Version 1.0
 */
@Component
public class ApplicationTicketProcessor extends UserPermissionExtendedAbstractTicketProcessor<Application> {

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APPLICATION.name();
    }

    @Override
    protected Class<Application> getEntryClassT() {
        return Application.class;
    }

}