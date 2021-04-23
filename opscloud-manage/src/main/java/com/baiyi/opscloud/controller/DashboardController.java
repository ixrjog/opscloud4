package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.vo.dashboard.DashboardVO;
import com.baiyi.opscloud.facade.dashboard.DashboardFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/11/23 11:28 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/dashboard")
@Api(tags = "仪表盘")
public class DashboardController {

    @Resource
    private DashboardFacade dashboardFacade;

    @ApiOperation(value = "查询HelpDesk报表")
    @GetMapping(value = "/helpdesk/report/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DashboardVO.HelpDeskReportGroupByWeeks> queryHelpDeskGroupByWeeks() {
        return new HttpResult<>(dashboardFacade.queryHelpDeskGroupByWeeks());
    }

    @ApiOperation(value = "刷新HelpDesk报表")
    @GetMapping(value = "/helpdesk/report/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DashboardVO.HelpDeskReportGroupByWeeks> refreshHelpDeskGroupByWeeks() {
        dashboardFacade.evictHelpdeskReport();
        return new HttpResult<>(dashboardFacade.queryHelpDeskGroupByWeeks());
    }

    @ApiOperation(value = "查询HelpDesk类型报表")
    @GetMapping(value = "/helpdesk/type/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DashboardVO.HelpDeskReportGroupByTypes> queryHelpDeskGroupByTypes() {
        return new HttpResult<>(dashboardFacade.queryHelpDeskGroupByTypes());
    }

    @ApiOperation(value = "查询HelpDesk类型报表")
    @GetMapping(value = "/helpdesk/type/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DashboardVO.HelpDeskReportGroupByTypes> refreshHelpDeskGroupByTypes() {
        dashboardFacade.evictHelpdeskTypeReport();
        return new HttpResult<>(dashboardFacade.queryHelpDeskGroupByTypes());
    }

}
