package com.baiyi.opscloud.workorder.processor.impl.base;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import com.baiyi.opscloud.workorder.processor.factory.WorkOrderTicketProcessorFactory;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2022/1/6 6:58 PM
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractTicketProcessor<T> implements ITicketProcessor, InitializingBean {

    private final WorkOrderTicketEntryService workOrderTicketEntryService;

    private final WorkOrderTicketService workOrderTicketService;

    private final UserService userService;

    /**
     * 处理工单条目
     *
     * @param ticketEntry
     * @param entry
     */
    abstract protected void process(WorkOrderTicketEntry ticketEntry, T entry);

    /**
     * 取条目ClassT
     *
     * @return
     */
    abstract protected Class<T> getEntryClassT();

    protected T toEntry(String entryContent, Class<T> classOfT) throws JsonSyntaxException {
        return new GsonBuilder().create().fromJson(entryContent, classOfT);
    }

    /**
     * 查询申请人
     *
     * @param ticketEntry
     * @return
     */
    protected User queryApplicant(WorkOrderTicketEntry ticketEntry) {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(ticketEntry.getWorkOrderTicketId());
        return userService.getByUsername(workOrderTicket.getUsername());
    }

    @Override
    public void process(WorkOrderTicketEntry ticketEntry) {
        this.process(ticketEntry, toEntry(ticketEntry.getContent(), getEntryClassT()));
    }

    protected void updateTicket(WorkOrderTicketEntry ticketEntry) {
        workOrderTicketEntryService.update(ticketEntry);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        WorkOrderTicketProcessorFactory.register(this);
    }

}
