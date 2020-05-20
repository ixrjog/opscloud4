package com.baiyi.opscloud.service.ticket.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketFlow;
import com.baiyi.opscloud.mapper.opscloud.OcWorkorderTicketFlowMapper;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketFlowService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/6 4:26 下午
 * @Version 1.0
 */
@Service
public class OcWorkorderTicketFlowServiceImpl implements OcWorkorderTicketFlowService {

    @Resource
    private OcWorkorderTicketFlowMapper ocWorkorderTicketFlowMapper;

    @Override
    public OcWorkorderTicketFlow queryOcWorkorderTicketFlowById(int id) {
        return ocWorkorderTicketFlowMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcWorkorderTicketFlow queryOcWorkorderTicketFlowByflowParentId(int flowParentId) {
        Example example = new Example(OcWorkorderTicketFlow.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("flowParentId", flowParentId);
        return ocWorkorderTicketFlowMapper.selectOneByExample(example);
    }

    @Override
    public OcWorkorderTicketFlow queryOcWorkorderTicketByUniqueKey(int ticketId, String flowName) {
        Example example = new Example(OcWorkorderTicketFlow.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", ticketId);
        criteria.andEqualTo("flowName", flowName);
        return ocWorkorderTicketFlowMapper.selectOneByExample(example);
    }

    @Override
    public List<OcWorkorderTicketFlow> queryOcWorkorderTicketByTicketId(int ticketId) {
        Example example = new Example(OcWorkorderTicketFlow.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", ticketId);
        return ocWorkorderTicketFlowMapper.selectByExample(example);
    }

    @Override
    public void addOcWorkorderTicketFlow(OcWorkorderTicketFlow ocWorkorderTicketFlow) {
        ocWorkorderTicketFlowMapper.insert(ocWorkorderTicketFlow);
    }

    @Override
    public void updateOcWorkorderTicketFlow(OcWorkorderTicketFlow ocWorkorderTicketFlow) {
        ocWorkorderTicketFlowMapper.updateByPrimaryKey(ocWorkorderTicketFlow);
    }

    @Override
    public void deleteOcWorkorderTicketFlowById(int id) {
        ocWorkorderTicketFlowMapper.deleteByPrimaryKey(id);
    }

}
