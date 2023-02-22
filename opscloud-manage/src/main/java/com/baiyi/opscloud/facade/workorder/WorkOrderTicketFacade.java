package com.baiyi.opscloud.facade.workorder;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
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

    /**
     * 分页查询 工单
     *
     * @param pageQuery
     * @return
     */
    DataTable<WorkOrderTicketVO.Ticket> queryTicketPage(WorkOrderTicketParam.TicketPageQuery pageQuery);

    /**
     * 分页查询 我的工单
     *
     * @param pageQuery
     * @return
     */
    DataTable<WorkOrderTicketVO.Ticket> queryMyTicketPage(WorkOrderTicketParam.MyTicketPageQuery pageQuery);

    /**
     * 创建
     *
     * @param createTicket
     * @return
     */
    WorkOrderTicketVO.TicketView createTicket(WorkOrderTicketParam.CreateTicket createTicket);

    /**
     * 保存
     *
     * @param saveTicket
     * @return
     */
    WorkOrderTicketVO.TicketView saveTicket(WorkOrderTicketParam.SubmitTicket saveTicket);

    /**
     * 提交
     *
     * @param submitTicket
     * @return
     */
    WorkOrderTicketVO.TicketView submitTicket(WorkOrderTicketParam.SubmitTicket submitTicket);

    /**
     * 审批
     *
     * @param approveTicket
     * @return
     */
    WorkOrderTicketVO.TicketView approveTicket(WorkOrderTicketParam.ApproveTicket approveTicket);

    /**
     * 移动端审批
     *
     * @param outApproveTicket
     */
    HttpResult approveTicket(WorkOrderTicketParam.OutApproveTicket outApproveTicket);


    WorkOrderTicketVO.TicketView getTicketEntries(int ticketId, String workOrderKey);

    /**
     * 查询工单视图
     *
     * @param ticketId
     * @return
     */
    WorkOrderTicketVO.TicketView getTicket(Integer ticketId);

    /**
     * 查询工单配置条目
     *
     * @param entryQuery
     * @return
     */
    List<WorkOrderTicketVO.Entry> queryTicketEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery);

    /**
     * 更新工单配置条目
     *
     * @param ticketEntry
     * @return
     */
    WorkOrderTicketVO.TicketView updateTicketEntry(WorkOrderTicketEntryParam.TicketEntry ticketEntry);

    /**
     * 新增工单配置条目
     *
     * @param ticketEntry
     * @return
     */
    void addTicketEntry(WorkOrderTicketEntryParam.TicketEntry ticketEntry);

    /**
     * 删除工单配置条目
     *
     * @param ticketEntryId
     * @return
     */
    void deleteTicketEntry(Integer ticketEntryId);

    void deleteTicketById(Integer ticketId);

    void deleteTicketByWorkOrderAndPhase(int workOrderId, String phase);

}
