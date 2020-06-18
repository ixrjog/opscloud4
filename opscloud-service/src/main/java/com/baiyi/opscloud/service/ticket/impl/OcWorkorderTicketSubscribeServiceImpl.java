package com.baiyi.opscloud.service.ticket.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketSubscribe;
import com.baiyi.opscloud.mapper.opscloud.OcWorkorderTicketSubscribeMapper;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketSubscribeService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/6 11:56 上午
 * @Version 1.0
 */
@Service
public class OcWorkorderTicketSubscribeServiceImpl implements OcWorkorderTicketSubscribeService {

    @Resource
    private OcWorkorderTicketSubscribeMapper ocWorkorderTicketSubscribeMapper;

    @Override
    public List<OcWorkorderTicketSubscribe> queryOcWorkorderTicketSubscribeByAppoval(int ticketId, int subscribeType) {
        Example example = new Example(OcWorkorderTicketSubscribe.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", ticketId);
        criteria.andEqualTo("subscribeType", subscribeType);
        return ocWorkorderTicketSubscribeMapper.selectByExample(example);
    }

    @Override
    public List<OcWorkorderTicketSubscribe> queryOcWorkorderTicketSubscribeByTicketId(int ticketId) {
        Example example = new Example(OcWorkorderTicketSubscribe.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", ticketId);
        return ocWorkorderTicketSubscribeMapper.selectByExample(example);
    }


    @Override
    public OcWorkorderTicketSubscribe queryOcWorkorderTicketSubscribeByParam(int ticketId, int userId, int subscribeType) {
        Example example = new Example(OcWorkorderTicketSubscribe.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", ticketId);
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("subscribeType", subscribeType);
        return ocWorkorderTicketSubscribeMapper.selectOneByExample(example);
    }


    @Override
    public OcWorkorderTicketSubscribe queryOcWorkorderTicketSubscribeById(int id) {
        return ocWorkorderTicketSubscribeMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcWorkorderTicketSubscribe(OcWorkorderTicketSubscribe ocWorkorderTicketSubscribe) {
        ocWorkorderTicketSubscribeMapper.insert(ocWorkorderTicketSubscribe);
    }

    @Override
    public void updateOcWorkorderTicketSubscribe(OcWorkorderTicketSubscribe ocWorkorderTicketSubscribe) {
        ocWorkorderTicketSubscribeMapper.updateByPrimaryKey(ocWorkorderTicketSubscribe);
    }

    @Override
    public void deleteOcWorkorderTicketSubscribeById(int id) {
        ocWorkorderTicketSubscribeMapper.deleteByPrimaryKey(id);
    }

}
