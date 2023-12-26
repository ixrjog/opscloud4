package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.mapper.WorkOrderTicketNodeMapper;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/14 4:11 PM
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class WorkOrderTicketNodeServiceImpl implements WorkOrderTicketNodeService {

    private final WorkOrderTicketNodeMapper workOrderTicketNodeMapper;

    @Override
    public WorkOrderTicketNode getById(int id) {
        return workOrderTicketNodeMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(WorkOrderTicketNode workOrderTicketNode) {
        workOrderTicketNodeMapper.insert(workOrderTicketNode);
    }

    @Override
    public void update(WorkOrderTicketNode workOrderTicketNode) {
        workOrderTicketNodeMapper.updateByPrimaryKey(workOrderTicketNode);
    }

    @Override
    public List<WorkOrderTicketNode> queryByWorkOrderTicketId(int workOrderTicketId) {
        Example example = new Example(WorkOrderTicketNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderTicketId", workOrderTicketId);
        return workOrderTicketNodeMapper.selectByExample(example);
    }

    @Override
    public WorkOrderTicketNode getByUniqueKey(int workOrderTicketId, String nodeName) {
        Example example = new Example(WorkOrderTicketNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderTicketId", workOrderTicketId)
                .andEqualTo("nodeName", nodeName);
        return workOrderTicketNodeMapper.selectOneByExample(example);
    }

    @Override
    public WorkOrderTicketNode getByUniqueKey(int workOrderTicketId, int parentId) {
        Example example = new Example(WorkOrderTicketNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderTicketId", workOrderTicketId)
                .andEqualTo("parentId", parentId);
        return workOrderTicketNodeMapper.selectOneByExample(example);
    }

    @Override
    public void deleteById(int id) {
        workOrderTicketNodeMapper.deleteByPrimaryKey(id);
    }

}