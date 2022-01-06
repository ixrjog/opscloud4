package com.baiyi.opscloud.workorder.processor;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;

/**
 * @Author baiyi
 * @Date 2022/1/6 6:52 PM
 * @Version 1.0
 */
public interface ITicketProcessor {

    /**
     * 工单Key
     * @return
     */
    String getKey();

    /**
     * 处理工单条目
     * @param ticketEntry
     */
    void process(WorkOrderTicketEntry ticketEntry);
}
