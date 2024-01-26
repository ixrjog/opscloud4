package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.model.WorkOrderToken;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/23 16:55
 * @Version 1.0
 */
@Component
public class ApolloReleaseFaultEmergencyTicketProcessor extends ApolloReleaseTicketProcessor {

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APOLLO_RELEASE_FE.name();
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, Application entry) throws TicketProcessException {
        WorkOrderToken.ApolloReleaseToken token = WorkOrderToken.ApolloReleaseToken.builder()
                .ticketId(ticketEntry.getWorkOrderTicketId())
                .key(entry.getId())
                .faultEmergency(true)
                .build();
        // 设置令牌，ApolloFacade 中使用
        workOrderApolloReleaseHolder.setToken(token);
    }

}
