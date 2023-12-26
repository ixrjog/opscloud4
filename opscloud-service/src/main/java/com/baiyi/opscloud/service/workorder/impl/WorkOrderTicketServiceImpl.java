package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderReportVO;
import com.baiyi.opscloud.mapper.WorkOrderTicketMapper;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 7:54 PM
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class WorkOrderTicketServiceImpl implements WorkOrderTicketService {

    private final WorkOrderTicketMapper workOrderTicketMapper;

    @Override
    public DataTable<WorkOrderTicket> queryPageByParam(WorkOrderTicketParam.TicketPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<WorkOrderTicket> data = workOrderTicketMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<WorkOrderReportVO.Report> queryReportByName() {
        return workOrderTicketMapper.queryReportByName();
    }

    @Override
    public List<WorkOrderReportVO.Report> statByMonth(Integer workOrderId) {
        return workOrderTicketMapper.queryReportByMonth(workOrderId);
    }

    @Override
    public void add(WorkOrderTicket workOrderTicket) {
        workOrderTicketMapper.insert(workOrderTicket);
    }

    @Override
    public void update(WorkOrderTicket workOrderTicket) {
        workOrderTicketMapper.updateByPrimaryKey(workOrderTicket);
    }


    @Override
    public void updateByPrimaryKeySelective(WorkOrderTicket workOrderTicket) {
        workOrderTicketMapper.updateByPrimaryKeySelective(workOrderTicket);
    }

    @Override
    public WorkOrderTicket getById(int id) {
        return workOrderTicketMapper.selectByPrimaryKey(id);
    }

    @Override
    public WorkOrderTicket getNewTicketByUser(String workOrderKey, String username) {
        return workOrderTicketMapper.getNewTicketByUser(workOrderKey, username);
    }

    @Override
    public void deleteById(int id) {
        workOrderTicketMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<WorkOrderTicket> queryByParam(int workOrderId, String phase) {
        Example example = new Example(WorkOrderTicket.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderId", workOrderId)
                .andEqualTo("ticketPhase", phase);
        return workOrderTicketMapper.selectByExample(example);
    }

}