package com.baiyi.opscloud.service.workevent.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkItem;
import com.baiyi.opscloud.mapper.WorkItemMapper;
import com.baiyi.opscloud.service.workevent.WorkItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/8/5 5:08 PM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class WorkItemServiceImpl implements WorkItemService {

    private final WorkItemMapper workItemMapper;

    @Override
    public List<WorkItem> listByWorkRoleIdAndParentId(Integer workRoleId, Integer parentId) {
        Example example = new Example(WorkItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workRoleId", workRoleId)
                .andEqualTo("parentId", parentId);
        return workItemMapper.selectByExample(example);
    }

    @Override
    public WorkItem getById(Integer id) {
        return workItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<WorkItem> listByWorkRoleId(Integer workRoleId) {
        Example example = new Example(WorkItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workRoleId", workRoleId);
        return workItemMapper.selectByExample(example);
    }

}