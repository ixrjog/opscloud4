package com.baiyi.opscloud.factory.ticket.impl.subscribe;

import com.baiyi.opscloud.bo.WorkorderTicketSubscribeBO;
import com.baiyi.opscloud.common.base.TicketPhase;
import com.baiyi.opscloud.common.base.TicketSubscribeType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketVO;
import com.baiyi.opscloud.factory.ticket.ITicketSubscribe;
import com.baiyi.opscloud.factory.ticket.WorkorderTicketSubscribeFactory;
import com.baiyi.opscloud.service.ticket.OcWorkorderApprovalMemberService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketFlowService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketSubscribeService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.baiyi.opscloud.service.workorder.OcWorkorderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/6 1:30 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseTicketSubscribe implements ITicketSubscribe, InitializingBean {

    @Resource
    protected OcWorkorderService ocWorkorderService;

    @Resource
    protected OcWorkorderTicketService ocWorkorderTicketService;

    @Resource
    protected OcWorkorderTicketSubscribeService ocWorkorderTicketSubscribeService;

    @Resource
    private OcWorkorderApprovalMemberService ocWorkorderApprovalMemberService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcWorkorderTicketFlowService ocWorkorderTicketFlowService;

    protected static final int ACTIVE = 1;

    @Override
    public OcWorkorderTicketSubscribe queryTicketSubscribe(OcWorkorderTicket ocWorkorderTicket, OcUser ocUser) {
        return null;
    }

    protected OcWorkorder getOcWorkorderById(int workorderId) {
        return ocWorkorderService.queryOcWorkorderById(workorderId);
    }

    /**
     * 发布审批组的订阅信息
     *
     * @param ocWorkorder
     * @param ocWorkorderTicket
     */
    protected void addTicketSubscribe(OcWorkorder ocWorkorder, OcWorkorderTicket ocWorkorderTicket) {
        ocWorkorderApprovalMemberService.queryOcWorkorderApprovalMemberByGroupId(ocWorkorder.getApprovalGroupId()).forEach(e ->
                addTicketSubscribe(ocWorkorderTicket, e.getUserId(), TicketSubscribeType.USERGROUP_APPROVAL.getType()));
    }

    @Override
    public void unsubscribe(OcWorkorderTicket ocWorkorderTicket) {
        for (TicketSubscribeType subscribeType : TicketSubscribeType.values())
            resetTicketSubscribe(ocWorkorderTicket, subscribeType.getType());
    }

    /**
     * 重置所有订阅用户无效
     * @param ocWorkorderTicket
     * @param subscribeType
     */
    protected void resetTicketSubscribe(OcWorkorderTicket ocWorkorderTicket, int subscribeType) {
        ocWorkorderTicketSubscribeService.queryOcWorkorderTicketSubscribeByAppoval(ocWorkorderTicket.getId(), subscribeType).forEach(e -> {
            e.setSubscribeActive(false);
            ocWorkorderTicketSubscribeService.updateOcWorkorderTicketSubscribe(e);
        });
    }

    protected void saveWorkorderTicket(OcWorkorderTicket ocWorkorderTicket) {
        ocWorkorderTicketService.updateOcWorkorderTicket(ocWorkorderTicket);
    }

    protected void addTicketSubscribe(OcWorkorderTicket ocWorkorderTicket, int userId, int subscribeType) {
        OcUser ocUser = ocUserService.queryOcUserById(userId);
        WorkorderTicketSubscribeBO subscribeBO = WorkorderTicketSubscribeBO.builder()
                .ticketId(ocWorkorderTicket.getId())
                .userId(ocUser.getId())
                .username(ocUser.getUsername())
                .subscribeType(subscribeType)
                .build();
        try {
            ocWorkorderTicketSubscribeService.addOcWorkorderTicketSubscribe(BeanCopierUtils.copyProperties(subscribeBO, OcWorkorderTicketSubscribe.class));
        } catch (Exception ignored) {
        }
    }

    @Override
    public Boolean isAllowApproval(OcUser ocUser, WorkorderTicketVO.Ticket ticket) {
        // 查询下级流程
        if (ticket.getFlowId() == null || ticket.getFlowId() == 0)
            return Boolean.FALSE;
        OcWorkorderTicketFlow ocWorkorderTicketFlow;
        if (ticket.getTicketPhase().equals(TicketPhase.APPLIED.getPhase())) {
            ocWorkorderTicketFlow = ocWorkorderTicketFlowService.queryOcWorkorderTicketFlowByflowParentId(ticket.getFlowId());
        } else {
            ocWorkorderTicketFlow = ocWorkorderTicketFlowService.queryOcWorkorderTicketFlowById(ticket.getFlowId());
        }
        if (ocWorkorderTicketFlow == null)
            return Boolean.FALSE;
        ITicketSubscribe iTicketSubscribe = WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(ocWorkorderTicketFlow.getFlowName());
        List<OcWorkorderTicketSubscribe> subscribes = iTicketSubscribe.queryTicketSubscribes(ticket);
        return subscribes.stream().filter(e -> e.getUserId().equals(ocUser.getId()) && e.getSubscribeActive()).count() == 1;
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        WorkorderTicketSubscribeFactory.register(this);
    }
}
