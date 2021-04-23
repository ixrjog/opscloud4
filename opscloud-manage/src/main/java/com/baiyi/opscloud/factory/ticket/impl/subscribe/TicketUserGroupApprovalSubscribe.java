package com.baiyi.opscloud.factory.ticket.impl.subscribe;

import com.baiyi.opscloud.common.base.TicketPhase;
import com.baiyi.opscloud.common.base.TicketSubscribeType;
import com.baiyi.opscloud.common.util.IDUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.vo.workorder.ApprovalStepsVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketVO;
import com.baiyi.opscloud.factory.ticket.ITicketSubscribe;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketSubscribeService;
import com.baiyi.opscloud.service.user.OcAccountService;
import com.baiyi.opscloud.service.workorder.OcWorkorderApprovalGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/6 8:08 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TicketUserGroupApprovalSubscribe extends BaseTicketSubscribe implements ITicketSubscribe {

    @Resource
    private OcWorkorderApprovalGroupService ocWorkorderApprovalGroupService;

    @Resource
    private OcWorkorderTicketSubscribeService ocWorkorderTicketSubscribeService;

    @Resource
    private OcAccountService ocAccountService;

    @Override
    public OcWorkorderTicketSubscribe queryTicketSubscribe(OcWorkorderTicket ocWorkorderTicket, OcUser ocUser) {
        OcWorkorder ocWorkorder = getOcWorkorderById(ocWorkorderTicket.getWorkorderId());
        if (ocWorkorder.getApprovalGroupId() == 0) return null;
        return ocWorkorderTicketSubscribeService.queryOcWorkorderTicketSubscribeByParam(ocWorkorderTicket.getId(), ocUser.getId(), TicketSubscribeType.USERGROUP_APPROVAL.getType());
    }

    @Override
    public String getKey() {
        return TicketPhase.USERGROUP_APPROVAL.getPhase();
    }

    // 工单创建状态-进入审批阶段
    @Override
    public BusinessWrapper<Boolean> subscribe(OcWorkorderTicket ocWorkorderTicket) {
        OcWorkorder ocWorkorder = getOcWorkorderById(ocWorkorderTicket.getWorkorderId());
        addTicketSubscribe(ocWorkorder, ocWorkorderTicket);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void invokeFlowStep(WorkorderTicketVO.Ticket ticket, String ticketPhase) {
        OcWorkorder ocWorkorder = getOcWorkorderById(ticket.getWorkorderId());
        if (IDUtils.isEmpty(ocWorkorder.getApprovalGroupId()))
            return;
        OcWorkorderApprovalGroup ocWorkorderApprovalGroup = ocWorkorderApprovalGroupService.queryOcWorkorderApprovalGroupById(ocWorkorder.getApprovalGroupId());
        ApprovalStepsVO.ApprovalStep approvalStep = ApprovalStepsVO.ApprovalStep.builder()
                .title(ocWorkorderApprovalGroup.getComment())
                .description(ocWorkorderApprovalGroup.getName())
                .build();
        ticket.getApprovalDetail().getApprovalSteps().add(approvalStep);

        if (TicketPhase.USERGROUP_APPROVAL.getPhase().equals(ticketPhase))
            ticket.getApprovalDetail().setActive(ticket.getApprovalDetail().getApprovalSteps().size());
    }

    @Override
    public List<OcWorkorderTicketSubscribe> queryTicketSubscribes(WorkorderTicketVO.Ticket ticket) {
        return ocWorkorderTicketSubscribeService.queryOcWorkorderTicketSubscribeByApproval(ticket.getId(), TicketSubscribeType.USERGROUP_APPROVAL.getType());
    }

    @Override
    protected List<OcWorkorderTicketSubscribe> getTicketSubscribe(OcWorkorderTicket ocWorkorderTicket) {
        return ocWorkorderTicketSubscribeService.queryOcWorkorderTicketSubscribeByApproval(ocWorkorderTicket.getId(), TicketSubscribeType.USERGROUP_APPROVAL.getType());

    }


}
