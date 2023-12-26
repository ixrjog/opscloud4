package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderGroup;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderGroupParam;
import com.baiyi.opscloud.mapper.WorkOrderGroupMapper;
import com.baiyi.opscloud.service.workorder.WorkOrderGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
    public DataTable<WorkOrderGroup> queryPageByParam(WorkOrderGroupParam.WorkOrderGroupPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(WorkOrderGroup.class);
        example.setOrderByClause("seq");
        return new DataTable<>(workOrderGroupMapper.selectByExample(example), page.getTotal());
    }

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

    @Override
    public void add(WorkOrderGroup workOrderGroup) {
        workOrderGroupMapper.insert(workOrderGroup);
    }

    @Override
    public void update(WorkOrderGroup workOrderGroup) {
        workOrderGroupMapper.updateByPrimaryKey(workOrderGroup);
    }

    @Override
    public void deleteById(int id) {
        workOrderGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int count() {
        Example example = new Example(WorkOrderGroup.class);
        return workOrderGroupMapper.selectCountByExample(example);
    }

}