package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.domain.vo.workorder.WorkOrderReportVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderReportFacade;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/2/14 11:41 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderReportFacadeImpl implements WorkOrderReportFacade {

    private final WorkOrderTicketService ticketService;

    @Override
    public List<WorkOrderReportVO.Report> queryTicketReportByName() {
        return ticketService.queryReportByName();
    }

}
