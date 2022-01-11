package com.baiyi.opscloud.workorder.processor;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.workorder.exception.VerifyTicketEntryException;
import com.google.gson.JsonSyntaxException;

/**
 * @Author baiyi
 * @Date 2022/1/6 6:52 PM
 * @Version 1.0
 */
public interface ITicketProcessor<T> {

    /**
     * 工单Key
     *
     * @return
     */
    String getKey();

    /**
     * 处理工单条目
     *
     * @param ticketEntry
     */
    void process(WorkOrderTicketEntry ticketEntry);

    /**
     * 校验工单条目
     *
     * @param ticketEntry
     */
    void verify(WorkOrderTicketEntry ticketEntry) throws VerifyTicketEntryException;

    T toEntry(String entryContent) throws JsonSyntaxException;

}
