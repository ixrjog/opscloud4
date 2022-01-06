package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.mapper.opscloud.WorkOrderTicketEntryMapper;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2022/1/6 7:43 PM
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class WorkOrderTicketEntryServiceImpl implements WorkOrderTicketEntryService {

    private final WorkOrderTicketEntryMapper workOrderTicketEntryMapper;

    @Override
    public void add(WorkOrderTicketEntry workOrderTicketEntry) {
        workOrderTicketEntryMapper.insert(workOrderTicketEntry);
    }

    @Override
    public void update(WorkOrderTicketEntry workOrderTicketEntry) {
        workOrderTicketEntryMapper.updateByPrimaryKey(workOrderTicketEntry);
    }

}
