package com.baiyi.opscloud.service.workevent.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkEventProperty;
import com.baiyi.opscloud.mapper.WorkEventPropertyMapper;
import com.baiyi.opscloud.service.workevent.WorkEventPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/8/18 6:23 PM
 * @Since 1.0
 */
@Service
@RequiredArgsConstructor
public class WorkEventPropertyServiceImpl implements WorkEventPropertyService {

    private final WorkEventPropertyMapper workEventPropertyMapper;

    @Override
    public void addList(List<WorkEventProperty> workEventPropertyList) {
        workEventPropertyMapper.insertList(workEventPropertyList);
    }

    @Override
    public void update(WorkEventProperty workEventProperty) {
        workEventPropertyMapper.updateByPrimaryKey(workEventProperty);
    }

    @Override
    public List<WorkEventProperty> listByWorkEventId(Integer workEventId) {
        Example example = new Example(WorkEventProperty.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workEventId", workEventId);
        return workEventPropertyMapper.selectByExample(example);
    }

    @Override
    public void deleteByWorkEventId(Integer workEventId) {
        Example example = new Example(WorkEventProperty.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workEventId", workEventId);
        workEventPropertyMapper.deleteByExample(example);
    }

}