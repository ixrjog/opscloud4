package com.baiyi.opscloud.workorder.approve;

import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;

/**
 * 审批接口
 * @Author baiyi
 * @Date 2022/1/19 2:58 PM
 * @Version 1.0
 */
public interface ITicketApprove {

    String getApprovalType();

    /**
     * 审批处理
     *
     * @param approveTicket
     */
    void approve(WorkOrderTicketParam.ApproveTicket approveTicket);

}