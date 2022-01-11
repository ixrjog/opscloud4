package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 10:35 AM
 * @Version 1.0
 */
public interface WorkOrderService {

    List<WorkOrder> queryByWorkOrderGroupId(int workOrderGroupId);

    WorkOrder getById(int id);

}
