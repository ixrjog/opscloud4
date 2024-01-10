package com.baiyi.opscloud.facade.workorder.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderGroup;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderViewVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderFacade;
import com.baiyi.opscloud.packer.workorder.WorkOrderGroupPacker;
import com.baiyi.opscloud.packer.workorder.WorkOrderPacker;
import com.baiyi.opscloud.service.workorder.WorkOrderGroupService;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.workorder.exception.TicketException;
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

    private final WorkOrderPacker workOrderPacker;

    private final WorkOrderGroupPacker workOrderGroupPacker;

    @Override
    public List<WorkOrderVO.WorkOrder> getWorkOrderOptions() {
        List<WorkOrder> workOrders = workOrderService.queryAll();
        return BeanCopierUtil.copyListProperties(workOrders, WorkOrderVO.WorkOrder.class);
    }

    @Override
    public DataTable<WorkOrderVO.WorkOrder> queryWorkOrderPage(WorkOrderParam.WorkOrderPageQuery pageQuery) {
        DataTable<WorkOrder> table = workOrderService.queryPageByParam(pageQuery);
        List<WorkOrderVO.WorkOrder> data = BeanCopierUtil.copyListProperties(table.getData(), WorkOrderVO.WorkOrder.class).stream().peek(e -> workOrderPacker.wrap(e, pageQuery)).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public DataTable<WorkOrderVO.Group> queryWorkOrderGroupPage(WorkOrderGroupParam.WorkOrderGroupPageQuery pageQuery) {
        DataTable<WorkOrderGroup> table = workOrderGroupService.queryPageByParam(pageQuery);
        List<WorkOrderVO.Group> data = BeanCopierUtil.copyListProperties(table.getData(), WorkOrderVO.Group.class).stream().peek(e -> workOrderGroupPacker.wrap(e, pageQuery)).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public WorkOrderViewVO.View getWorkOrderView() {
        List<WorkOrderGroup> groups = workOrderGroupService.queryAll();
        List<WorkOrderVO.Group> workOrderGroups = groups.stream().map(workOrderPacker::wrap).collect(Collectors.toList());
        return WorkOrderViewVO.View.builder().workOrderGroups(workOrderGroups).build();
    }

    @Override
    public void saveWorkOrderGroup(WorkOrderGroupParam.Group group) {
        WorkOrderGroup workOrderGroup = BeanCopierUtil.copyProperties(group, WorkOrderGroup.class);
        if (group.getId() == null) {
            if (group.getSeq() == null) {
                workOrderGroup.setSeq(workOrderGroupService.count() + 1);
            }
            workOrderGroupService.add(workOrderGroup);
        } else {
            workOrderGroupService.update(workOrderGroup);
        }
    }

    @Override
    public void deleteWorkOrderGroup(Integer workOrderGroupId) {
        if (0 != workOrderService.countByWorkOrderGroupId(workOrderGroupId)) {
            throw new TicketException("工单组下存在工单，无法删除！");
        }
        workOrderGroupService.deleteById(workOrderGroupId);
    }

    @Override
    public void updateWorkOrder(WorkOrderParam.WorkOrder workOrder) {
        workOrderService.update(BeanCopierUtil.copyProperties(workOrder, WorkOrder.class));
    }

}
