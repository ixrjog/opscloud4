package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderGroup;
import com.baiyi.opscloud.domain.vo.order.WorkOrderVO;
import com.baiyi.opscloud.domain.vo.order.WorkOrderViewVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderFacade;
import com.baiyi.opscloud.packer.order.WorkOrderPacker;
import com.baiyi.opscloud.service.workorder.WorkOrderGroupService;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
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

    private final WorkOrderService orderService;

    private final WorkOrderGroupService orderGroupService;

    private final WorkOrderPacker orderPacker;

    @Override
    public WorkOrderViewVO.View getWorkOrderView() {
        List<WorkOrderGroup> groups = orderGroupService.queryAll();
        List<WorkOrderVO.Group> workOrderGroups = groups.stream().map(orderPacker::wrap).collect(Collectors.toList());
        return WorkOrderViewVO.View.builder()
                .workOrderGroups(workOrderGroups)
                .build();
    }

}
