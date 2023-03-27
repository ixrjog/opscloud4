package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderReportVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderReportFacade;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/2/14 11:41 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderReportFacadeImpl implements WorkOrderReportFacade {

    private final WorkOrderTicketService ticketService;

    private final WorkOrderService workOrderService;

    @Override
    public List<WorkOrderReportVO.Report> queryTicketReportByName() {
        return ticketService.queryReportByName();
    }

    @Override
    public WorkOrderReportVO.MonthReport queryTicketReportByMonth() {
        List<String> dateCatList = ticketService.statByMonth(-1)
                .stream().map(WorkOrderReportVO.Report::getCName).collect(Collectors.toList());
        return WorkOrderReportVO.MonthReport.builder()
                .dateCat(dateCatList)
                .nameCat(queryWorkOrderNameStatistics())
                .build();
    }

    private Map<String, WorkOrderReportVO.MonthStatistics> queryWorkOrderNameStatistics() {
        List<WorkOrder> workorderList = workOrderService.queryAll();
        return workorderList.stream().collect(
                Collectors.toMap(WorkOrder::getName,
                        e -> WorkOrderReportVO.MonthStatistics.builder()
                                .values(ticketService.statByMonth(e.getId())
                                        .stream()
                                        .map(WorkOrderReportVO.Report::getValue)
                                        .collect(Collectors.toList()))
                                .color(workOrderService.getById(e.getId()).getColor())
                                .build()
                        , (k1, k2) -> k1));
    }

}
