package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.holder.WorkOrderApolloReleaseHolder;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.model.WorkOrderToken;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.base.BaseTicketProcessor;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/6/13 17:54
 * @Version 1.0
 */
@Component
public class ApolloReleaseTicketProcessor extends BaseTicketProcessor<Application> {

    @Resource
    private WorkOrderApolloReleaseHolder workOrderApolloReleaseHolder;

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APOLLO_RELEASE.name();
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, Application entry) throws TicketProcessException {
        WorkOrderToken.ApolloReleaseToken token = WorkOrderToken.ApolloReleaseToken.builder()
                .ticketId(ticketEntry.getWorkOrderTicketId())
                .key(entry.getId())
                .build();
        // 设置令牌，ApolloFacade 中使用
        workOrderApolloReleaseHolder.setToken(token);
    }

    @Override
    protected Class<Application> getEntryClassT() {
        return Application.class;
    }

    @Override
    protected void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
    }

}