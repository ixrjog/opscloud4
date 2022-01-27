package com.baiyi.opscloud.facade.workorder;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderViewVO;

/**
 * @Author baiyi
 * @Date 2022/1/6 10:54 AM
 * @Version 1.0
 */
public interface WorkOrderFacade {

    /**
     * 工单分页查询
     * @param pageQuery
     * @return
     */
    DataTable<WorkOrderVO.WorkOrder> queryWorkOrderPage(WorkOrderParam.WorkOrderPageQuery pageQuery);

    /**
     * 查询工单视图
     *
     * @return
     */
    WorkOrderViewVO.View getWorkOrderView();

}
