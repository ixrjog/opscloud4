package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketEntryVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketVO;
import com.baiyi.opscloud.factory.ticket.ITicketHandler;
import com.baiyi.opscloud.factory.ticket.WorkorderTicketFactory;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketEntryService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/28 6:49 下午
 * @Version 1.0
 */
@Component
public class WorkorderTicketDecorator {

    @Resource
    private OcWorkorderTicketEntryService ocWorkorderTicketEntryService;

    @Resource
    private TicketApprovalDecorator ticketApprovalDecorator;

    public OcWorkorderTicketVO.Ticket decorator(OcWorkorderTicketVO.Ticket ticket) {
        List<OcWorkorderTicketEntry> ticketEntryList = ocWorkorderTicketEntryService.queryOcWorkorderTicketEntryByTicketId(ticket.getId());
        ticket.setTicketEntries(convertTicketEntries(ticketEntryList));
        ticketApprovalDecorator.decorator(ticket);
        return ticket;
    }

    private List<OcWorkorderTicketEntryVO.Entry> convertTicketEntries(List<OcWorkorderTicketEntry> ticketEntryList) {
        return ticketEntryList.stream().map(e -> {
            ITicketHandler ticketHandler = WorkorderTicketFactory.getTicketHandlerByKey(e.getEntryKey());
            return ticketHandler.convertTicketEntry(e);
        }).collect(Collectors.toList());
    }


}
