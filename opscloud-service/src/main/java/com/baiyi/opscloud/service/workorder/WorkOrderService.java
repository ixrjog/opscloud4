package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 10:35 AM
 * @Version 1.0
 */
public interface WorkOrderService {

    DataTable<WorkOrder> queryPageByParam(WorkOrderParam.WorkOrderPageQuery pageQuery);

    List<WorkOrder> queryByWorkOrderGroupId(int workOrderGroupId);

    int countByWorkOrderGroupId(int workOrderGroupId);

    WorkOrder getById(int id);

    WorkOrder getByKey(String key);

    void update(WorkOrder workOrder);

    List<WorkOrder> queryAll();

}