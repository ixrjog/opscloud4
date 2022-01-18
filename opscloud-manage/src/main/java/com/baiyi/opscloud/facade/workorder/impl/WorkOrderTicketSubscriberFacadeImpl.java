package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketSubscriber;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketSubscriberFacade;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketSubscriberService;
import com.baiyi.opscloud.workorder.constants.SubscribeStatusConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/1/18 10:23 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderTicketSubscriberFacadeImpl implements WorkOrderTicketSubscriberFacade {

    private final WorkOrderTicketSubscriberService ticketSubscriberService;

    /**
     * 创建订阅人（创建人）
     * @param workOrderTicket
     * @param user
     */
    @Override
    public void createSubscriber(WorkOrderTicket workOrderTicket, User user) {
        WorkOrderTicketSubscriber subscriber = WorkOrderTicketSubscriber.builder()
                .workOrderTicketId(workOrderTicket.getId())
                .userId(user.getId())
                .username(user.getUsername())
                .subscribeStatus(SubscribeStatusConstants.CREATE.name())
                .isActive(true)
                .comment(SubscribeStatusConstants.CREATE.getComment())
                .build();
        ticketSubscriberService.add(subscriber);
    }

}
