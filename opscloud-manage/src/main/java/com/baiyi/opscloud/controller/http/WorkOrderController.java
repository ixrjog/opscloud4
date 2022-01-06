package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.vo.order.WorkOrderViewVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author baiyi
 * @Date 2022/1/6 3:37 PM
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/workorder")
@Api(tags = "工单")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderFacade workOrderFacade;

    @ApiOperation(value = "查询工单视图")
    @GetMapping(value = "/view/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderViewVO.View> getWorkOrderView() {
        return new HttpResult<>(workOrderFacade.getWorkOrderView());
    }

}
