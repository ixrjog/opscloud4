package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractUserPermissionExtendedBaseTicketProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 应用权限申请工单票据处理
 *
 * @Author baiyi
 * @Date 2022/1/7 10:26 AM
 * @Version 1.0
 */
@Component
public class ApplicationPermissionTicketProcessor extends AbstractUserPermissionExtendedBaseTicketProcessor<Application> {

    @Resource
    private ApplicationService applicationService;

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        Application entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isEmpty(entry.getApplicationKey())) {
            throw new TicketVerifyException("校验工单条目失败: 未指定应用Key！");
        }
        Application application = applicationService.getByKey(entry.getApplicationKey());
        if (application == null) {
            throw new TicketVerifyException("校验工单条目失败: 应用不存在！");
        }
    }

    @Override
    public void update(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        handleUpdate(ticketEntry);
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APPLICATION_PERMISSION.name();
    }

    @Override
    protected Class<Application> getEntryClassT() {
        return Application.class;
    }

}