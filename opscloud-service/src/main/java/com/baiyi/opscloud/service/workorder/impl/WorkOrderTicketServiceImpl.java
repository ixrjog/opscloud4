package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.mapper.opscloud.WorkOrderTicketMapper;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public WorkOrderTicket getById(int id) {
        return workOrderTicketMapper.selectByPrimaryKey(id);
    }

}
