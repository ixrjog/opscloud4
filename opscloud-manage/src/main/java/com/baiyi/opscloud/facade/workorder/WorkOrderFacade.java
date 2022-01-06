package com.baiyi.opscloud.facade.workorder;

import com.baiyi.opscloud.domain.vo.order.WorkOrderViewVO;

/**
 * @Author baiyi
 * @Date 2022/1/6 10:54 AM
 * @Version 1.0
 */
public interface WorkOrderFacade {

    /**
     * 查询工单视图
     * @return
     */
    WorkOrderViewVO.View getWorkOrderView();

}
