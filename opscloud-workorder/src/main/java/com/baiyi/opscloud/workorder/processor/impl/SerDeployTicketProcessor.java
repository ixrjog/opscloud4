package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.helper.WorkOrderSerDeployHelper;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.model.WorkOrderSerDeployToken;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.base.BaseTicketProcessor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/6/14 19:03
 * @Version 1.0
 */
@Slf4j
@Component
public class SerDeployTicketProcessor extends BaseTicketProcessor<Application> {

    @Resource
    private WorkOrderSerDeployHelper workOrderSerDeployHelper;

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SER_DEPLOY.name();
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, Application entry) throws TicketProcessException {
        WorkOrderSerDeployToken token = WorkOrderSerDeployToken.builder()
                .ticketId(ticketEntry.getWorkOrderTicketId())
                .applicationId(entry.getId())
                .build();
        // 设置令牌
        workOrderSerDeployHelper.set(token);
    }

    @Override
    protected Class<Application> getEntryClassT() {
        return Application.class;
    }

    @Override
    protected void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
    }

}
