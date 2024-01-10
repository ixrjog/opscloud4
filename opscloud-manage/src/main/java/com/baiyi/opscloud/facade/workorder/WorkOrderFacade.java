package com.baiyi.opscloud.facade.workorder;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderViewVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 10:54 AM
 * @Version 1.0
 */
public interface WorkOrderFacade {

    List<WorkOrderVO.WorkOrder> getWorkOrderOptions();

    /**
     * 工单分页查询
     *
     * @param pageQuery
     * @return
     */
    DataTable<WorkOrderVO.WorkOrder> queryWorkOrderPage(WorkOrderParam.WorkOrderPageQuery pageQuery);


    /**
     * 工单组分页查询
     *
     * @param pageQuery
     * @return
     */
    DataTable<WorkOrderVO.Group> queryWorkOrderGroupPage(WorkOrderGroupParam.WorkOrderGroupPageQuery pageQuery);

    /**
     * 查询工单视图
     *
     * @return
     */
    WorkOrderViewVO.View getWorkOrderView();

    /**
     * 保存工单组
     *
     * @param group
     */
    void saveWorkOrderGroup(WorkOrderGroupParam.Group group);

    /**
     * 删除工单组
     *
     * @param workOrderGroupId
     */
    void deleteWorkOrderGroup(Integer workOrderGroupId);


    /**
     * 保存工单组
     *
     * @param workOrder
     */
    void updateWorkOrder(WorkOrderParam.WorkOrder workOrder);

}
