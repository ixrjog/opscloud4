package com.baiyi.opscloud.facade.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;

/**
 * @Author baiyi
 * @Date 2022/1/18 10:23 AM
 * @Version 1.0
 */
public interface WorkOrderTicketSubscriberFacade {

    void publish(WorkOrderTicket workOrderTicket, User user);

    void publish(WorkOrderTicket workOrderTicket);

}
