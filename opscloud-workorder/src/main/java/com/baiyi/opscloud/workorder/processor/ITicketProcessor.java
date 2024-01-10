package com.baiyi.opscloud.workorder.processor;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
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
     * 更新工单配置条目（可重写）
     * @param ticketEntry
     */
    default void update(WorkOrderTicketEntryParam.TicketEntry ticketEntry){
        throw new TicketProcessException("工单配置不允许变更！");
    }

    /**
     * 校验工单条目
     *
     * @param ticketEntry
     */
    void verify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException;

    T toEntry(String entryContent) throws JsonSyntaxException;

}