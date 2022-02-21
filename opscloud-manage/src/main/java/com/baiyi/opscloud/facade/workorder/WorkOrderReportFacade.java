package com.baiyi.opscloud.facade.workorder;

import com.baiyi.opscloud.domain.vo.workorder.WorkOrderReportVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/2/14 11:40 AM
 * @Version 1.0
 */
public interface WorkOrderReportFacade {

    List<WorkOrderReportVO.Report> queryTicketReportByName();

    WorkOrderReportVO.MonthReport queryTicketReportByMonth();
}
