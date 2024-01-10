package com.baiyi.opscloud.workorder.helper;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 工单通知助手
 *
 * @Author baiyi
 * @Date 2022/1/26 3:08 PM
 * @Version 1.0
 */

@Slf4j
@Component
public class TicketNoticeHelper {

    public static final Map<String, Consumer<WorkOrderTicket>> CONTEXT = new ConcurrentHashMap<>();

    /**
     * 发送通知
     *
     * @param ticket
     */
    @Async
    public void send(WorkOrderTicket ticket) {
        final String phase = ticket.getTicketPhase();
        if (CONTEXT.containsKey(phase)) {
            CONTEXT.get(phase).accept(ticket);
        }
    }

}