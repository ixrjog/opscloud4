package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderParam;
import com.baiyi.opscloud.mapper.opscloud.WorkOrderMapper;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 10:35 AM
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderMapper workOrderMapper;

    @Override
    public DataTable<WorkOrder> queryPageByParam(WorkOrderParam.WorkOrderPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(WorkOrder.class);
        if (IdUtil.isNotEmpty(pageQuery.getWorkOrderGroupId())) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderGroupId", pageQuery.getWorkOrderGroupId());
        }
        example.setOrderByClause("work_order_group_id, seq");
        return new DataTable<>(workOrderMapper.selectByExample(example), page.getTotal());
    }

    @Override
    public List<WorkOrder> queryByWorkOrderGroupId(int workOrderGroupId) {
        Example example = new Example(WorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderGroupId", workOrderGroupId)
                .andEqualTo("isActive", true);
        example.setOrderByClause("seq");
        return workOrderMapper.selectByExample(example);
    }

    @Override
    public int countByWorkOrderGroupId(int workOrderGroupId){
        Example example = new Example(WorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderGroupId", workOrderGroupId);
        return workOrderMapper.selectCountByExample(example);
    }

    @Override
    public WorkOrder getById(int id) {
        return workOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public WorkOrder getByKey(String key) {
        Example example = new Example(WorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderKey", key);
        return workOrderMapper.selectOneByExample(example);
    }

    @Override
    public void update(WorkOrder workOrder) {
        workOrderMapper.updateByPrimaryKey(workOrder);
    }

    @Override
    public List<WorkOrder> queryAll() {
        return workOrderMapper.selectAll();
    }
}
