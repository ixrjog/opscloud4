package com.baiyi.opscloud.workorder.helper;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.workorder.constants.OrderPhaseCodeConstants;
import com.baiyi.opscloud.workorder.helper.strategy.TicketNoticeStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration.TaskPools.CORE;

/**
 * 工单通知助手
 *
 * @Author baiyi
 * @Date 2022/1/26 3:08 PM
 * @Version 1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketNoticeHelper {

    private final TicketNoticeStrategy ticketNoticeStrategy;

    /**
     * 发送通知
     *
     * @param ticket
     */
    @Async(CORE)
    public void notice(WorkOrderTicket ticket) {
        try {
            OrderPhaseCodeConstants ticketPhase = OrderPhaseCodeConstants.getEnum(ticket.getTicketPhase());
            ticketNoticeStrategy.getStrategyNotice(ticketPhase).accept(ticket, ticketPhase.getResult());
        } catch (Exception e) {
            log.error("未定义改阶段通知模板");
        }
    }
}
