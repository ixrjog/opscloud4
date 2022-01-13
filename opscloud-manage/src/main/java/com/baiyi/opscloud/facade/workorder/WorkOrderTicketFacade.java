package com.baiyi.opscloud.facade.workorder;

import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/11 2:12 PM
 * @Version 1.0
 */
public interface WorkOrderTicketFacade {

    WorkOrderTicketVO.TicketView createTicket(WorkOrderTicketParam.CreateTicket createTicket);

    WorkOrderTicketVO.TicketView getTicket(Integer ticketId);

    List<WorkOrderTicketVO.Entry> queryTicketEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery);

    /**
     * 更新工单条目
     *
     * @param ticketEntry
     * @return
     */
    WorkOrderTicketVO.TicketView updateTicketEntry(WorkOrderTicketParam.TicketEntry ticketEntry);

    /**
     * 新增工单条目
     *
     * @param ticketEntry
     * @return
     */
    void addTicketEntry(WorkOrderTicketParam.TicketEntry ticketEntry);

    /**
     * 删除工单条目
     *
     * @param ticketEntryId
     * @return
     */
    void deleteTicketEntry(Integer ticketEntryId);

}
