package com.baiyi.opscloud.service.ticket;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/27 4:53 下午
 * @Version 1.0
 */
public interface OcWorkorderTicketEntryService {

    List<OcWorkorderTicketEntry> queryOcWorkorderTicketEntryByTicketId(int workorderTicketId);

    int countOcWorkorderTicketEntryByTicketId(int workorderTicketId);

    OcWorkorderTicketEntry queryOcWorkorderTicketEntryById(int id);

    void updateOcWorkorderTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry);

    void addOcWorkorderTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry);

    void deleteOcWorkorderTicketEntryById(int id);
}
