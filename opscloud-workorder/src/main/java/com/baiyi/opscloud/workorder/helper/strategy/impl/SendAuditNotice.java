package com.baiyi.opscloud.workorder.helper.strategy.impl;

import com.baiyi.opscloud.common.configuration.properties.OpscloudConfigurationProperties;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.datasource.manager.base.NoticeManager;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.notice.INoticeMessage;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketSubscriberService;
import com.baiyi.opscloud.workorder.constants.ApprovalTypeConstants;
import com.baiyi.opscloud.workorder.constants.NodeTypeConstants;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
import com.baiyi.opscloud.workorder.helper.strategy.base.AbstractSendNotice;
import com.baiyi.opscloud.workorder.model.TicketNoticeModel;
import com.baiyi.opscloud.workorder.util.WorkflowUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.baiyi.opscloud.datasource.manager.base.NoticeManager.MsgKeys.TICKET_APPROVE;

/**
 * @Author baiyi
 * @Date 2022/1/27 4:46 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class SendAuditNotice extends AbstractSendNotice {

    @Resource
    private WorkOrderTicketSubscriberService ticketSubscriberService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private OpscloudConfigurationProperties opscloudConfigurationProperties;

    public final static String KEY_PREFIX = "Opscloud.V4.workOrder#ticketId.{}.username.{}";

    public static String buildKey(Integer ticketId, String username) {
        return StringFormatter.arrayFormat(KEY_PREFIX, ticketId, username);
    }

    @Override
    public Set<String> getPhases() {
        return Sets.newHashSet(OrderTicketPhaseCodeConstants.TOAUDIT.getPhase());
    }

    @Override
    public void send(WorkOrderTicket ticket) {
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        Map<String, WorkflowVO.Node> nodeMap = WorkflowUtil.toNodeMap(workOrder.getWorkflow());
        WorkOrderTicketNode node = ticketNodeService.getById(ticket.getNodeId());
        if (nodeMap.containsKey(node.getNodeName())) {
            INoticeMessage noticeMessage = buildNoticeMessage(ticket);
            WorkflowVO.Node workflowNode = nodeMap.get(node.getNodeName());
            if (NodeTypeConstants.SYS.getCode() == workflowNode.getType()) {
                // 广播
                List<User> auditUsers = userService.queryByTagKeys(workflowNode.getTags());
                send(ticket.getId(), auditUsers, TICKET_APPROVE, noticeMessage);
            }
            if (NodeTypeConstants.USER_LIST.getCode() == workflowNode.getType()) {
                // 单播
                User auditUser = userService.getByUsername(node.getUsername());
                send(ticket.getId(), Lists.newArrayList(auditUser), TICKET_APPROVE, noticeMessage);
            }
        }
    }

    protected void send(Integer ticketId, List<User> users, String msgKey, INoticeMessage noticeMessage) {
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        TicketNoticeModel.ApproveNoticeMessage approveNoticeMessage = (TicketNoticeModel.ApproveNoticeMessage) noticeMessage;
        users.forEach(user -> {
            if (NoticeManager.MsgKeys.TICKET_APPROVE.equals(msgKey)) {
                // 有效期1day
                try {
                    WorkOrderTicketSubscriber queryParam = WorkOrderTicketSubscriber.builder()
                            .username(user.getUsername())
                            .workOrderTicketId(ticketId)
                            .subscribeStatus("AUDIT")
                            .build();
                    WorkOrderTicketSubscriber subscriber = ticketSubscriberService.getByUniqueKey(queryParam);
                    if (subscriber != null && !"-".equals(subscriber.getToken())) {
                        redisUtil.set(buildKey(ticketId, user.getUsername()), subscriber.getToken(), NewTimeUtil.DAY_TIME / 1000);
                        String api = opscloudConfigurationProperties.getOutapi().getWorkorder().getApproval();
                        String apiAgree = String.format(api, ticketId, user.getUsername(), ApprovalTypeConstants.AGREE.name(), subscriber.getToken());
                        String apiReject = String.format(api, ticketId, user.getUsername(), ApprovalTypeConstants.REJECT.name(), subscriber.getToken());
                        approveNoticeMessage.setApiAgree(apiAgree);
                        approveNoticeMessage.setApiReject(apiReject);
                        noticeManager.sendMessage(user, msgKey, approveNoticeMessage);
                    }
                } catch (Exception e) {
                    log.error("工单移动端审批Token写入失败: {}", e.getMessage());
                }
            }
        });
    }

    @Override
    protected INoticeMessage buildNoticeMessage(WorkOrderTicket ticket) {
        // 工单创建者
        User user = userService.getByUsername(ticket.getUsername());
        final String userDisplayName = StringFormatter.arrayFormat("{}<{}>", user.getUsername(), Joiner.on(":").skipNulls().join(user.getDisplayName(), user.getName()));
        // 工单名称
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        // 工单条目
        List<WorkOrderTicketEntry> ticketEntries = ticketEntryService.queryByWorkOrderTicketId(ticket.getId());
        return TicketNoticeModel.ApproveNoticeMessage.builder()
                .ticketId(ticket.getId())
                .createUser(userDisplayName)
                .workOrderName(workOrder.getName())
                .ticketEntities(ticketEntries)
                .build();
    }

}