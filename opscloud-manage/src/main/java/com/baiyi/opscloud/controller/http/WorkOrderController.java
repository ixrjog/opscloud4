package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderGroupParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderReportVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderViewVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderFacade;
import com.baiyi.opscloud.facade.workorder.WorkOrderReportFacade;
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

    private final WorkOrderReportFacade workOrderReportFacade;

    @ApiOperation(value = "查询工单选项")
    @GetMapping(value = "/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<WorkOrderVO.WorkOrder>> getWorkOrderOptions() {
        return new HttpResult<>(workOrderFacade.getWorkOrderOptions());
    }

    @ApiOperation(value = "分页查询工单配置")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<WorkOrderVO.WorkOrder>> queryWorkOrderPage(@RequestBody @Valid WorkOrderParam.WorkOrderPageQuery pageQuery) {
        return new HttpResult<>(workOrderFacade.queryWorkOrderPage(pageQuery));
    }

    @ApiOperation(value = "更新工单配置")
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> queryWorkOrderPage(@RequestBody @Valid WorkOrderVO.WorkOrder workOrder) {
        workOrderFacade.updateWorkOrder(workOrder);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询工单组配置")
    @PostMapping(value = "/group/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<WorkOrderVO.Group>> queryWorkOrderGroupPage(@RequestBody @Valid WorkOrderGroupParam.WorkOrderGroupPageQuery pageQuery) {
        return new HttpResult<>(workOrderFacade.queryWorkOrderGroupPage(pageQuery));
    }

    @ApiOperation(value = "保存工单组配置")
    @PostMapping(value = "/group/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveWorkOrderGroup(@RequestBody @Valid WorkOrderVO.Group group) {
        workOrderFacade.saveWorkOrderGroup(group);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除工单组配置")
    @DeleteMapping(value = "/group/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteWorkOrderGroup(@RequestParam int workOrderGroupId) {
        workOrderFacade.deleteWorkOrderGroup(workOrderGroupId);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "查询工单视图")
    @GetMapping(value = "/view/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderViewVO.View> getWorkOrderView() {
        return new HttpResult<>(workOrderFacade.getWorkOrderView());
    }

    @ApiOperation(value = "分页查询工单票据")
    @PostMapping(value = "/ticket/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<WorkOrderTicketVO.Ticket>> queryTicketPage(@RequestBody @Valid WorkOrderTicketParam.TicketPageQuery pageQuery) {
        return new HttpResult<>(workOrderTicketFacade.queryTicketPage(pageQuery));
    }

    @ApiOperation(value = "分页查询我的工单票据")
    @PostMapping(value = "/ticket/my/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<WorkOrderTicketVO.Ticket>> queryMyTicketPage(@RequestBody @Valid WorkOrderTicketParam.MyTicketPageQuery pageQuery) {
        return new HttpResult<>(workOrderTicketFacade.queryMyTicketPage(pageQuery));
    }

    @ApiOperation(value = "创建工单票据")
    @PostMapping(value = "/ticket/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketView> createTicket(@RequestBody @Valid WorkOrderTicketParam.CreateTicket createTicket) {
        return new HttpResult<>(workOrderTicketFacade.createTicket(createTicket));
    }

    @ApiOperation(value = "删除指定的工单票据")
    @DeleteMapping(value = "/ticket/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTicketById(@RequestParam int ticketId) {
        workOrderTicketFacade.deleteTicketById(ticketId);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "暂存工单票据")
    @PostMapping(value = "/ticket/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketView> saveTicket(@RequestBody @Valid WorkOrderTicketParam.SubmitTicket saveTicket) {
        return new HttpResult<>(workOrderTicketFacade.saveTicket(saveTicket));
    }

    @ApiOperation(value = "提交工单票据")
    @PostMapping(value = "/ticket/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketView> submitTicket(@RequestBody @Valid WorkOrderTicketParam.SubmitTicket submitTicket) {
        return new HttpResult<>(workOrderTicketFacade.submitTicket(submitTicket));
    }

    @ApiOperation(value = "审批工单票据")
    @PostMapping(value = "/ticket/approve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketView> approveTicket(@RequestBody @Valid WorkOrderTicketParam.ApproveTicket approveTicket) {
        return new HttpResult<>(workOrderTicketFacade.approveTicket(approveTicket));
    }

    @ApiOperation(value = "查询工单所有配置条目")
    @GetMapping(value = "/ticket/entries/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketView> getTicketEntries(@RequestParam @Valid int ticketId, @Valid String workOrderKey) {
        return new HttpResult<>(workOrderTicketFacade.getTicketEntries(ticketId, workOrderKey));
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

    @ApiOperation(value = "工单报表(按名称汇总)")
    @GetMapping(value = "/report/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<WorkOrderReportVO.Report>> queryTicketReportByName() {
        return new HttpResult<>(workOrderReportFacade.queryTicketReportByName());
    }

    @ApiOperation(value = "工单报表(按月份汇总)")
    @GetMapping(value = "/report/month", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderReportVO.MonthReport> queryTicketReportByMonth() {
        return new HttpResult<>(workOrderReportFacade.queryTicketReportByMonth());
    }

}
