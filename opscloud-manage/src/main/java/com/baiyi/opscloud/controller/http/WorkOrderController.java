package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderViewVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderFacade;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    private final WorkOrderTicketFacade workOrderTicketFacade;

    @ApiOperation(value = "查询工单视图")
    @GetMapping(value = "/view/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderViewVO.View> getWorkOrderView() {
        return new HttpResult<>(workOrderFacade.getWorkOrderView());
    }

    @ApiOperation(value = "创建工单票据")
    @PostMapping(value = "/ticket/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketView> createWorkOrderTicket(@RequestBody @Valid WorkOrderTicketParam.CreateTicket createTicket) {
        return new HttpResult<>(workOrderTicketFacade.createWorkOrderTicket(createTicket));
    }

    @ApiOperation(value = "查询工单票据条目")
    @PostMapping(value = "/ticket/entry/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<WorkOrderTicketVO.Entry>> queryWorkOrderTicketEntry(@RequestBody @Valid WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        return new HttpResult<>(workOrderTicketFacade.queryWorkOrderTicketEntry(entryQuery));
    }

}
