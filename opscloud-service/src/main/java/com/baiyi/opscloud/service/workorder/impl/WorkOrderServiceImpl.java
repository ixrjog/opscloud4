package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.mapper.opscloud.WorkOrderMapper;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
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
    public List<WorkOrder> queryByWorkOrderGroupId(int workOrderGroupId) {
        Example example = new Example(WorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderGroupId", workOrderGroupId);
        example.setOrderByClause("seq");
        return workOrderMapper.selectByExample(example);
    }

    @Override
    public WorkOrder getById(int id) {
        return workOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public  WorkOrder getByKey(String key){
        Example example = new Example(WorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderKey", key);
        return workOrderMapper.selectOneByExample(example);
    }
}
