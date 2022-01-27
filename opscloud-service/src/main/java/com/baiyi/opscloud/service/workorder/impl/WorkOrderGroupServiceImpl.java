package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderGroup;
import com.baiyi.opscloud.mapper.opscloud.WorkOrderGroupMapper;
import com.baiyi.opscloud.service.workorder.WorkOrderGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 10:37 AM
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class WorkOrderGroupServiceImpl implements WorkOrderGroupService {

    private final WorkOrderGroupMapper workOrderGroupMapper;

    @Override
    public List<WorkOrderGroup> queryAll() {
        Example example = new Example(WorkOrderGroup.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("seq");
        return workOrderGroupMapper.selectByExample(example);
    }

    @Override
    public WorkOrderGroup getById(int id) {
        return workOrderGroupMapper.selectByPrimaryKey(id);
    }

}
