package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.monitor.MonitorHostParam;
import com.baiyi.opscloud.domain.param.monitor.MonitorUserParam;
import com.baiyi.opscloud.domain.vo.monitor.MonitorVO;
import com.baiyi.opscloud.facade.MonitorFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/11/24 10:32 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/monitor")
@Api(tags = "监控管理")
public class MonitorController {

    @Resource
    private MonitorFacade monitorFacade;

    @ApiOperation(value = "分页查询监控主机列表")
    @PostMapping(value = "/host/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<MonitorVO.Host>> queryMonitorHostPage(@RequestBody @Valid MonitorHostParam.MonitorHostPageQuery pageQuery) {
        return new HttpResult<>(monitorFacade.queryMonitorHostPage(pageQuery));
    }

    @ApiOperation(value = "同步监控主机信息")
    @GetMapping(value = "/host/status/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncMonitorHostStatus() {
        monitorFacade.syncMonitorHostStatus();
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "设置监控主机（禁用/启用）状态")
    @PutMapping(value = "/host/status/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setMonitorHostStatus(@RequestParam @Valid int id) {
        return new HttpResult<>(monitorFacade.setMonitorHostStatus(id));
    }

    @ApiOperation(value = "创建监控主机")
    @PostMapping(value = "/host/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createMonitorHost(@RequestBody @Valid MonitorHostParam.CreateMonitorHost createMonitorHost) {
        return new HttpResult<>(monitorFacade.createMonitorHost(createMonitorHost));
    }

    @ApiOperation(value = "推送配置到监控主机")
    @PutMapping(value = "/host/push", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pushMonitorHost(@RequestBody @Valid MonitorHostParam.PushMonitorHost pushMonitorHost) {
        return new HttpResult<>(monitorFacade.pushMonitorHost(pushMonitorHost));
    }

    @ApiOperation(value = "分页查询Zabbix用户列表")
    @PostMapping(value = "/user/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<MonitorVO.User>> queryMonitorUserPage(@RequestBody @Valid MonitorUserParam.MonitorUserPageQuery pageQuery) {
        return new HttpResult<>(monitorFacade.queryMonitorUserPage(pageQuery));
    }

    @ApiOperation(value = "同步Zabbix用户列表")
    @GetMapping(value = "/user/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncMonitorUsers() {
        monitorFacade.syncZabbixUsers();
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "批量更新监控主机状态(发布系统专用)")
    @PutMapping(value = "/host/mass/update/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> massUpdateMonitorHostStatus(@RequestBody @Valid MonitorHostParam.MassUpdateMonitorHostStatus massUpdateMonitorHostStatus) {
        return new HttpResult<>(monitorFacade.massUpdateMonitorHostStatus(massUpdateMonitorHostStatus));
    }

}
