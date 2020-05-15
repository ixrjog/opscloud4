package com.baiyi.opscloud.factory.ticket.impl.subscribe;

import com.baiyi.opscloud.bo.WorkorderTicketSubscribeBO;
import com.baiyi.opscloud.common.base.TicketSubscribeType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.factory.ticket.ITicketSubscribe;
import com.baiyi.opscloud.factory.ticket.WorkorderTicketSubscribeFactory;
import com.baiyi.opscloud.service.ticket.OcWorkorderApprovalMemberService;
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
        List<OcWorkorderApprovalMember> list = ocWorkorderApprovalMemberService.queryOcWorkorderApprovalMemberByGroupId(ocWorkorder.getApprovalGroupId());
        for (OcWorkorderApprovalMember member : list) {
            addTicketSubscribe(ocWorkorderTicket, member.getUserId(), TicketSubscribeType.USERGROUP_APPROVAL.getType());
        }
    }

    @Override
    public void unsubscribe(OcWorkorderTicket ocWorkorderTicket) {
        for (TicketSubscribeType subscribeType : TicketSubscribeType.values())
            resetTicketSubscribe(ocWorkorderTicket, subscribeType.getType());
    }


    /**
     * 重置所有订阅用户无效
     *
     * @param ocWorkorderTicket
     */
    protected void resetTicketSubscribe(OcWorkorderTicket ocWorkorderTicket, int subscribeType) {
        List<OcWorkorderTicketSubscribe> list = ocWorkorderTicketSubscribeService.queryOcWorkorderTicketSubscribeByAppoval(ocWorkorderTicket.getId(), subscribeType);
        for (OcWorkorderTicketSubscribe subscribe : list) {
            subscribe.setSubscribeActive(false);
            ocWorkorderTicketSubscribeService.updateOcWorkorderTicketSubscribe(subscribe);
        }
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
        } catch (Exception e) {
        }
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        WorkorderTicketSubscribeFactory.register(this);
    }
}
