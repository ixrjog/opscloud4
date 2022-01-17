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
    public HttpResult<WorkOrderTicketVO.TicketView> createTicket(@RequestBody @Valid WorkOrderTicketParam.CreateTicket createTicket) {
        return new HttpResult<>(workOrderTicketFacade.createTicket(createTicket));
    }

    @ApiOperation(value = "暂存工单票据")
    @PostMapping(value = "/ticket/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketView> saveTicket(@RequestBody @Valid WorkOrderTicketParam.SaveTicket saveTicket) {
        return new HttpResult<>(workOrderTicketFacade.saveTicket(saveTicket));
    }

    @ApiOperation(value = "提交工单票据")
    @PostMapping(value = "/ticket/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketView> submitTicket(@RequestBody @Valid WorkOrderTicketParam.SubmitTicket submitTicket) {
        return new HttpResult<>(workOrderTicketFacade.submitTicket(submitTicket));
    }

    @ApiOperation(value = "新增工单票据配置条目")
    @PostMapping(value = "/ticket/entry/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTicketEntry(@RequestBody @Valid WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        workOrderTicketFacade.addTicketEntry(ticketEntry);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新工单票据配置条目")
    @PostMapping(value = "/ticket/entry/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketView> updateTicketEntry(@RequestBody @Valid WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        return new HttpResult<>(workOrderTicketFacade.updateTicketEntry(ticketEntry));
    }

    @ApiOperation(value = "查询工单票据条目")
    @PostMapping(value = "/ticket/entry/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<WorkOrderTicketVO.Entry>> queryTicketEntry(@RequestBody @Valid WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        return new HttpResult<>(workOrderTicketFacade.queryTicketEntry(entryQuery));
    }

    @ApiOperation(value = "删除工单票据条目")
    @DeleteMapping(value = "/ticket/entry/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTicketEntry(@RequestParam @Valid int ticketEntryId) {
        workOrderTicketFacade.deleteTicketEntry(ticketEntryId);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "查询工单票据视图")
    @GetMapping(value = "/ticket/view/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketView> getTicketView(@RequestParam @Valid int ticketId) {
        return new HttpResult<>(workOrderTicketFacade.getTicket(ticketId));
    }

}
