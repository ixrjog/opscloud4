package com.baiyi.opscloud.decorator.workorder;

import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.base.TicketPhase;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeAgoUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorder;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketFlow;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderVO;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.facade.UserPermissionFacade;
import com.baiyi.opscloud.facade.impl.WorkorderFacadeImpl;
import com.baiyi.opscloud.factory.ticket.ITicketHandler;
import com.baiyi.opscloud.factory.ticket.ITicketSubscribe;
import com.baiyi.opscloud.factory.ticket.WorkorderTicketFactory;
import com.baiyi.opscloud.factory.ticket.WorkorderTicketSubscribeFactory;
import com.baiyi.opscloud.factory.ticket.impl.handler.BaseTicketHandler;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketEntryService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketFlowService;
import com.baiyi.opscloud.service.workorder.OcWorkorderService;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/28 6:49 下午
 * @Version 1.0
 */
@Component
public class WorkorderTicketDecorator {

    @Resource
    private OcWorkorderTicketEntryService ocWorkorderTicketEntryService;

    @Resource
    private OcWorkorderService ocWorkorderService;

    @Resource
    private TicketApprovalDecorator ticketApprovalDecorator;

    @Resource
    private UserFacade userFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private OcWorkorderTicketFlowService ocWorkorderTicketFlowService;

    public WorkorderTicketVO.Ticket decorator(WorkorderTicketVO.Ticket ticket) {
        // 工单
        OcWorkorder ocWorkorder = ocWorkorderService.queryOcWorkorderById(ticket.getWorkorderId());
        ticket.setWorkorder(BeanCopierUtils.copyProperties(ocWorkorder, WorkorderVO.Workorder.class));
        // 发起人
        ticket.setUser(new GsonBuilder().create().fromJson(ticket.getUserDetail(), UserVO.User.class));
        if (ticket.getStartTime() != null)
            ticket.setAgo(TimeAgoUtils.format(ticket.getStartTime()));
        ticket.setIsInApproval(isInApproval(ticket));  // 审批流程中（给前端按钮使用）
        if (ticket.getIsInApproval()) { // 允许审批
            ITicketSubscribe iTicketSubscribe = WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(ticket.getTicketPhase());
            ticket.setIsAllowApproval(iTicketSubscribe.isAllowApproval(userFacade.getOcUserBySession(), ticket));
        } else {
            ticket.setIsAllowApproval(false);
        }
        ticket.setIsAllowDelete(isAllowDelete(ticket));  // 允许删除
        // 工单条目
        List<OcWorkorderTicketEntry> ticketEntryList = ocWorkorderTicketEntryService.queryOcWorkorderTicketEntryByTicketId(ticket.getId());
        ticket.setTicketEntries(convertTicketEntries(ticketEntryList));
        ticketApprovalDecorator.decorator(ticket);
        if (ticket.getTicketPhase().equals(TicketPhase.FINALIZED.getPhase())) {
            // 执行结果
            List<OcWorkorderTicketEntry> errTicketEntryList =
                    ticketEntryList.stream().filter(x -> x.getEntryStatus().equals(BaseTicketHandler.ENTRY_STATUS_ERROR)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(errTicketEntryList))
                ticket.setExecutorResult(true);
            else
                ticket.setExecutorResult(false);
            // 审批状态
            List<OcWorkorderTicketFlow> flowList = ocWorkorderTicketFlowService.queryOcWorkorderTicketByTicketId(ticket.getId());
            List<OcWorkorderTicketFlow> disagreeFlowList =
                    flowList.stream().filter(x -> x.getApprovalStatus().equals(WorkorderFacadeImpl.DISAGREE_TYPE)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(disagreeFlowList))
                ticket.setApprovalStatus(true);
            else
                ticket.setApprovalStatus(false);
        }
        return ticket;
    }

    private boolean isInApproval(WorkorderTicketVO.Ticket ticket) {
        // 审批结束 工单完成
        if (ticket.getTicketStatus() != 0) return false;
        if (ticket.getTicketPhase().equals(TicketPhase.CREATED.getPhase()))
            return false;
        if (ticket.getTicketPhase().equals(TicketPhase.FINALIZED.getPhase()))
            return false;
        if (ticket.getTicketPhase().equals(TicketPhase.CONFIGURATION.getPhase()))
            return false;
        return true;
    }

    /**
     * 已完成的工单不能删除
     * 管理员才能删除
     *
     * @param ticket
     * @return
     */
    private boolean isAllowDelete(WorkorderTicketVO.Ticket ticket) {
        // 审批结束 工单完成
        if (ticket.getTicketPhase().equals(TicketPhase.FINALIZED.getPhase()))
            return false;
        OcUser ocUser = userFacade.getOcUserBySession();
        BusinessWrapper<Boolean> wrapper = userPermissionFacade.checkAccessLevel(ocUser, AccessLevel.ADMIN.getLevel());
        return wrapper.isSuccess();
    }

    private List<WorkorderTicketEntryVO.Entry> convertTicketEntries(List<OcWorkorderTicketEntry> ticketEntryList) {
        return ticketEntryList.stream().map(e -> {
            ITicketHandler ticketHandler = WorkorderTicketFactory.getTicketHandlerByKey(e.getEntryKey());
            return ticketHandler.convertTicketEntry(e);
        }).collect(Collectors.toList());
    }

}
