package com.baiyi.opscloud.order;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderViewVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderFacade;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/1/6 2:05 PM
 * @Version 1.0
 */
public class OrderFacadeTest extends BaseUnit {

    @Resource
    private WorkOrderFacade workOrderFacade;

    @Test
    void queryOrderViewTest() {
        WorkOrderViewVO.View view = workOrderFacade.getWorkOrderView();
        print(view);
    }

}
