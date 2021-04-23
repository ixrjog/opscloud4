package com.baiyi.opscloud.factory.ticket.impl.subscribe;

import com.baiyi.opscloud.bo.workorder.WorkorderTicketFlowMsgBO;
import com.baiyi.opscloud.bo.workorder.WorkorderTicketSubscribeBO;
import com.baiyi.opscloud.common.base.AccountType;
import com.baiyi.opscloud.common.base.DingtalkMsgType;
import com.baiyi.opscloud.common.base.TicketPhase;
import com.baiyi.opscloud.common.base.TicketSubscribeType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.BeetlUtils;
import com.baiyi.opscloud.common.util.ObjectUtils;
import com.baiyi.opscloud.dingtalk.DingtalkMsgApi;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkMsgParam;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketVO;
import com.baiyi.opscloud.factory.ticket.ITicketSubscribe;
import com.baiyi.opscloud.factory.ticket.WorkorderTicketSubscribeFactory;
import com.baiyi.opscloud.service.file.OcFileTemplateService;
import com.baiyi.opscloud.service.ticket.*;
import com.baiyi.opscloud.service.user.OcAccountService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.baiyi.opscloud.service.workorder.OcWorkorderService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Resource
    private OcWorkorderTicketEntryService ocWorkorderTicketEntryService;

    @Resource
    private DingtalkMsgApi dingtalkMsgApi;

    @Resource
    private OcFileTemplateService ocFileTemplateService;

    @Resource
    private OcAccountService ocAccountService;

    protected static final int ACTIVE = 1;

    private static final String FILE_TEMPLATE_DINGTALK_WORKORDER_MSG = "DINGTALK_WORKORDER_MSG";

    private static final int DEFAULT = 0;

    private static final String DINGTALK_UID = "xincheng";

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
     *
     * @param ocWorkorderTicket
     * @param subscribeType
     */
    protected void resetTicketSubscribe(OcWorkorderTicket ocWorkorderTicket, int subscribeType) {
        ocWorkorderTicketSubscribeService.queryOcWorkorderTicketSubscribeByApproval(ocWorkorderTicket.getId(), subscribeType).forEach(e -> {
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

    @Override
    public void sendTicketFlowMsg(OcWorkorderTicket ocWorkorderTicket) {
        List<OcWorkorderTicketEntry> ticketEntryList = ocWorkorderTicketEntryService.queryOcWorkorderTicketEntryByTicketId(ocWorkorderTicket.getId());
        Set<String> useridList = getUseridList(ocWorkorderTicket);
        if (CollectionUtils.isEmpty(useridList)) return;
        DingtalkParam.SendAsyncMsg param = getDingtalkMsg(ocWorkorderTicket, ticketEntryList, useridList);
        if (param != null)
            dingtalkMsgApi.sendAsyncMsg(param);
    }

    protected List<OcWorkorderTicketSubscribe> getTicketSubscribe(OcWorkorderTicket ocWorkorderTicket) {
        return Collections.emptyList();
    }

    private Set<String> getUseridList(OcWorkorderTicket ocWorkorderTicket) {
        List<OcWorkorderTicketSubscribe> subscribeList = getTicketSubscribe(ocWorkorderTicket);
        Set<String> userIdList = Sets.newHashSet();
        subscribeList.forEach(subscribe -> {
            OcAccount ocAccount = ocAccountService.queryOcAccountByUserId(AccountType.DINGTALK.getType(), subscribe.getUserId());
            if (ocAccount == null)
                log.error("用户暂未绑定钉钉，用户id:{}", subscribe.getUserId());
            else
                userIdList.add(ocAccount.getUsername());
        });
        return userIdList;
    }


    private DingtalkParam.SendAsyncMsg getDingtalkMsg(
            OcWorkorderTicket ocWorkorderTicket, List<OcWorkorderTicketEntry> ticketEntryList, Set<String> useridList) {
        OcWorkorder ocWorkorder = ocWorkorderService.queryOcWorkorderById(ocWorkorderTicket.getWorkorderId());
        WorkorderTicketFlowMsgBO.DingtalkMsg dingtalkMsg = WorkorderTicketFlowMsgBO.DingtalkMsg.builder()
                .ticketId(ocWorkorderTicket.getId())
                .workorderName(ocWorkorder.getName())
                .ticketEntryList(ticketEntryList)
                .build();
        OcUser ocUser = ocUserService.queryOcUserByUsername(ocWorkorderTicket.getUsername());
        String displayName = ocUser == null ? Strings.EMPTY : ocUser.getDisplayName();
        dingtalkMsg.setDisplayName(displayName);
        Map<String, Object> contentMap = ObjectUtils.objectToMap(dingtalkMsg);
        OcFileTemplate template = ocFileTemplateService.queryOcFileTemplateByUniqueKey(FILE_TEMPLATE_DINGTALK_WORKORDER_MSG, DEFAULT);
        try {
            DingtalkMsgParam.MarkdownMsg markdownMsg = DingtalkMsgParam.MarkdownMsg.builder()
                    .title(ocWorkorder.getName())
                    .text(BeetlUtils.renderTemplate(template.getContent(), contentMap))
                    .build();
            DingtalkMsgParam msgParam = DingtalkMsgParam.builder()
                    .msgtype(DingtalkMsgType.MARKDOWN.getType())
                    .markdown(markdownMsg)
                    .build();
            DingtalkParam.SendAsyncMsg msg = new DingtalkParam.SendAsyncMsg();
            msg.setUid(DINGTALK_UID);
            msg.setMsg(msgParam);
            msg.setUseridList(useridList);
            return msg;
        } catch (IOException e) {
            log.error("生成钉钉发送消息参数失败", e);
            return null;
        }
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
