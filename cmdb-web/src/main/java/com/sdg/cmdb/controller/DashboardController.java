package com.sdg.cmdb.controller;



import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.service.DashboardService;
import com.sdg.cmdb.service.ZabbixServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ZabbixServerService zabbixServerService;

    @Autowired
    private DashboardService dashboardService;

    /**
     * 查询当前问题
     * @return
     */
    @RequestMapping(value = "/problems/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getProblems() {
        return new HttpResult(zabbixServerService.getMyProblems());
    }

    @RequestMapping(value = "/base/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getBase() {
        return new HttpResult(dashboardService.getDashboard());
    }



}
