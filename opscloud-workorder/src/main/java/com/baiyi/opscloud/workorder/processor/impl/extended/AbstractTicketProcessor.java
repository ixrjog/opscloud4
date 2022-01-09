package com.baiyi.opscloud.workorder.processor.impl.extended;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.constants.EntryStatusConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import com.baiyi.opscloud.workorder.processor.factory.WorkOrderTicketProcessorFactory;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/1/6 6:58 PM
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractTicketProcessor<T> implements ITicketProcessor, InitializingBean {

    @Resource
    private WorkOrderTicketEntryService workOrderTicketEntryService;

    @Resource
    private WorkOrderTicketService workOrderTicketService;

    @Resource
    private UserService userService;

    /**
     * 处理工单条目
     *
     * @param ticketEntry
     * @param entry
     */
    abstract protected void process(WorkOrderTicketEntry ticketEntry, T entry) throws TicketProcessException;

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
     * 查询创建人
     *
     * @param ticketEntry
     * @return
     */
    protected User queryCreateUser(WorkOrderTicketEntry ticketEntry) {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(ticketEntry.getWorkOrderTicketId());
        return userService.getByUsername(workOrderTicket.getUsername());
    }

    @Override
    public void process(WorkOrderTicketEntry ticketEntry) {
        try {
            this.process(ticketEntry, toEntry(ticketEntry.getContent(), getEntryClassT()));
            ticketEntry.setEntryStatus(EntryStatusConstants.SUCCESSFUL.getStatus());
        } catch (TicketProcessException e) {
            ticketEntry.setResult(e.getMessage());
            ticketEntry.setEntryStatus(EntryStatusConstants.FAILED.getStatus());
        }
        updateTicket(ticketEntry);
    }

    private void updateTicket(WorkOrderTicketEntry ticketEntry) {
        workOrderTicketEntryService.update(ticketEntry);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        WorkOrderTicketProcessorFactory.register(this);
    }

}
