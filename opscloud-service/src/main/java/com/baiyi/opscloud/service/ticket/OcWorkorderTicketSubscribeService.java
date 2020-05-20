package com.baiyi.opscloud.service.ticket;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketSubscribe;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/6 11:56 上午
 * @Version 1.0
 */
public interface OcWorkorderTicketSubscribeService {

    /**
     * 查询所有订阅工单的审批人
     * @param ticketId
     * @return
     */
    List<OcWorkorderTicketSubscribe> queryOcWorkorderTicketSubscribeByAppoval(int ticketId, int subscribeType);

    List<OcWorkorderTicketSubscribe> queryOcWorkorderTicketSubscribeByTicketId(int ticketId);

    OcWorkorderTicketSubscribe queryOcWorkorderTicketSubscribeByParam(int ticketId,int userId, int subscribeType);

    OcWorkorderTicketSubscribe queryOcWorkorderTicketSubscribeById(int id);

    void addOcWorkorderTicketSubscribe(OcWorkorderTicketSubscribe ocWorkorderTicketSubscribe);

    void updateOcWorkorderTicketSubscribe(OcWorkorderTicketSubscribe ocWorkorderTicketSubscribe);

    void deleteOcWorkorderTicketSubscribeById(int id);
}
