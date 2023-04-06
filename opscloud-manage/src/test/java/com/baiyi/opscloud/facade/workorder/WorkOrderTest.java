package com.baiyi.opscloud.facade.workorder;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.packer.workorder.TicketPacker;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

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

    @Resource
    private TicketPacker workOrderTicketPacker;

    @Test
    void ticketViewTest() {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(1);
        WorkOrderTicketVO.TicketView ticketView = workOrderTicketPacker.toTicketView(workOrderTicket);
        print(ticketView);
    }

}
