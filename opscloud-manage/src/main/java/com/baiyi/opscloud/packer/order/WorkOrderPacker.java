package com.baiyi.opscloud.packer.order;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderGroup;
import com.baiyi.opscloud.domain.vo.order.WorkOrderVO;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 1:47 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderPacker {

    private final WorkOrderService workOrderService;

    public WorkOrderVO.Group wrap(WorkOrderGroup workOrderGroup) {
        WorkOrderVO.Group group = BeanCopierUtil.copyProperties(workOrderGroup, WorkOrderVO.Group.class);
        List<WorkOrder> orders = workOrderService.queryByWorkOrderGroupId(group.getId());
        group.setWorkOrders(BeanCopierUtil.copyListProperties(orders, WorkOrderVO.WorkOrder.class));
        return group;
    }

}
