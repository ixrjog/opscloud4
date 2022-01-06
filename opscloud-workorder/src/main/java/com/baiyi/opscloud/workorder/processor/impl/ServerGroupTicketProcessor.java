package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.processor.impl.base.AbstractTicketProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/1/6 7:02 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class ServerGroupTicketProcessor extends AbstractTicketProcessor<ServerGroup> {

    public ServerGroupTicketProcessor(WorkOrderTicketEntryService workOrderTicketEntryService, WorkOrderTicketService workOrderTicketService, UserService userService) {
        super(workOrderTicketEntryService, workOrderTicketService, userService);
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SERVER_GROUP.name();
    }

    @Override
    protected Class<ServerGroup> getEntryClassT(){
        return ServerGroup.class;
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, ServerGroup entry) {

    }

}
