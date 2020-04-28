package com.baiyi.opscloud.service.ticket.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;
import com.baiyi.opscloud.mapper.opscloud.OcWorkorderTicketMapper;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/27 5:30 下午
 * @Version 1.0
 */
@Service
public class OcWorkorderTicketServiceImpl implements OcWorkorderTicketService {

    @Resource
    private OcWorkorderTicketMapper ocWorkorderTicketMapper;

    @Override
    public OcWorkorderTicket queryOcWorkorderTicketById(int id) {
        return ocWorkorderTicketMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcWorkorderTicket(OcWorkorderTicket ocWorkorderTicket) {
        ocWorkorderTicketMapper.insert(ocWorkorderTicket);
    }

    @Override
    public void updateOcWorkorderTicket(OcWorkorderTicket ocWorkorderTicket) {
        ocWorkorderTicketMapper.updateByPrimaryKey(ocWorkorderTicket);
    }
}
