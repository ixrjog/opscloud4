package com.baiyi.opscloud.facade.workorder;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/1/17 7:12 PM
 * @Version 1.0
 */
class WorkOrderTest extends BaseUnit {

    @Resource
    private WorkOrderTicketService workOrderTicketService;

    @Resource
    private WorkOrderTicketFacade workOrderTicketFacade;

    @Test
    void ticketViewTest() {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(1);
        WorkOrderTicketVO.TicketView ticketView = workOrderTicketFacade.toTicketView(workOrderTicket);
        print(ticketView);
    }

}
