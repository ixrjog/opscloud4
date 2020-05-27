package com.baiyi.opscloud.service.ticket.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketParam;
import com.baiyi.opscloud.mapper.opscloud.OcWorkorderTicketMapper;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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
    public List<OcWorkorderTicket> queryOcWorkorderTicketByParam(OcWorkorderTicket ocWorkorderTicket) {
        Example example = new Example(OcWorkorderTicket.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workorderId", ocWorkorderTicket.getWorkorderId());
        criteria.andEqualTo("userId", ocWorkorderTicket.getUserId());
        criteria.andEqualTo("ticketPhase", ocWorkorderTicket.getTicketPhase());
        return ocWorkorderTicketMapper.selectByExample(example);
    }

    @Override
    public void addOcWorkorderTicket(OcWorkorderTicket ocWorkorderTicket) {
        ocWorkorderTicketMapper.insert(ocWorkorderTicket);
    }

    @Override
    public void updateOcWorkorderTicket(OcWorkorderTicket ocWorkorderTicket) {
        ocWorkorderTicketMapper.updateByPrimaryKey(ocWorkorderTicket);
    }

    @Override
    public void deleteOcWorkorderTicketById(int id) {
        ocWorkorderTicketMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DataTable<OcWorkorderTicket> queryMyOcWorkorderTicketByParam(WorkorderTicketParam.QueryMyTicketPage pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcWorkorderTicket> list = ocWorkorderTicketMapper.queryMyTicketByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public DataTable<OcWorkorderTicket> queryOcWorkorderTicketByParam(WorkorderTicketParam.QueryTicketPage pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcWorkorderTicket> list = ocWorkorderTicketMapper.queryTicketByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }
}
