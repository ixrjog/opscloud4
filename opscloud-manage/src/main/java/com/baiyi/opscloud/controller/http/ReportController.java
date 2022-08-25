package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.domain.vo.terminal.TerminalReportVO;
import com.baiyi.opscloud.domain.vo.workevent.WorkEventReportVO;
import com.baiyi.opscloud.facade.terminal.TerminalReportFacade;
import com.baiyi.opscloud.facade.workevent.WorkEventFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/8 09:56
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/report")
@Api(tags = "报表管理")
@RequiredArgsConstructor
public class ReportController {

    private final TerminalReportFacade terminalReportFacade;

    private final WorkEventFacade workEventFacade;

    @ApiOperation(value = "查询终端报表")
    @GetMapping(value = "/terminal/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<TerminalReportVO.TerminalReport> getTerminalReport() {
        return new HttpResult<>(terminalReportFacade.statTerminalReport());
    }

    @ApiOperation(value = "查询工作事件周报表")
    @GetMapping(value = "/workevent/weekly/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkEventReportVO.WeeklyReport> getWorkEventWeeklyReport(@RequestParam int workRoleId) {
        return new HttpResult<>(workEventFacade.getWorkEventWeeklyReport(workRoleId));
    }

    @ApiOperation(value = "工作事件报表(按类目汇总)")
    @GetMapping(value = "/workevent/item", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<WorkEventReportVO.ItemReport>> getWorkEventItemReport(@RequestParam int workRoleId) {
        return new HttpResult<>(workEventFacade.getWorkEventItemReport(workRoleId));
    }

    @ApiOperation(value = "工作事件处理时效周报表")
    @GetMapping(value = "/workevent/timeliness", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ReportVO.CommonReport>> getWorkEventTimeReport() {
        return new HttpResult<>(workEventFacade.getWorkEventTimeReport());
    }

    @ApiOperation(value = "工作事件拦截率周报表")
    @GetMapping(value = "/workevent/intercept", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ReportVO.CommonReport>> getWorkEventInterceptReport() {
        return new HttpResult<>(workEventFacade.getWorkEventInterceptReport());
    }

    @ApiOperation(value = "工作事件故障统计周报表")
    @GetMapping(value = "/workevent/solve", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ReportVO.CommonReport>> getWorkEventFaultReport() {
        return new HttpResult<>(workEventFacade.getWorkEventFaultReport());
    }

    @ApiOperation(value = "工作事件处理时效周报表")
    @GetMapping(value = "/workevent/fault", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ReportVO.CommonReport>> getWorkEventSolveReport() {
        return new HttpResult<>(workEventFacade.getWorkEventSolveReport());
    }
}
