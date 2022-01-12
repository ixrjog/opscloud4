package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderGroup;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderViewVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderFacade;
import com.baiyi.opscloud.packer.workorder.WorkOrderPacker;
import com.baiyi.opscloud.service.workorder.WorkOrderGroupService;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.query.factory.WorkOrderTicketEntryQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/1/6 10:54 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderFacadeImpl implements WorkOrderFacade {

    private final WorkOrderGroupService workOrderGroupService;

    private final WorkOrderService workOrderService;

    private final WorkOrderTicketService workOrderTicketService;

    private final WorkOrderPacker workOrderPacker;

    @Override
    public WorkOrderViewVO.View getWorkOrderView() {
        List<WorkOrderGroup> groups = workOrderGroupService.queryAll();
        List<WorkOrderVO.Group> workOrderGroups = groups.stream().map(workOrderPacker::wrap).collect(Collectors.toList());
        return WorkOrderViewVO.View.builder()
                .workOrderGroups(workOrderGroups)
                .build();
    }

}
