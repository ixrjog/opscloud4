package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketNode;
import com.baiyi.opscloud.mapper.opscloud.WorkOrderTicketNodeMapper;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public void add(WorkOrderTicketNode workOrderTicketNode) {
        workOrderTicketNodeMapper.insert(workOrderTicketNode);
    }

    @Override
    public void update(WorkOrderTicketNode workOrderTicketNode) {
        workOrderTicketNodeMapper.updateByPrimaryKey(workOrderTicketNode);
    }

}
