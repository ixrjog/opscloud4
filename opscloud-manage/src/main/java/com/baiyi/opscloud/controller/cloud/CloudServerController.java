package com.baiyi.opscloud.controller.cloud;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.CloudServerParam;
import com.baiyi.opscloud.domain.param.cloud.CloudServerStatsParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudServerStatsVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudServerVO;
import com.baiyi.opscloud.facade.CloudServerFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/17 9:30 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/cloud/server")
@Api(tags = "云服务器")
public class CloudServerController {

    @Resource
    private CloudServerFacade cloudServerFacade;

    @ApiOperation(value = "分页查询云主机列表")
    @GetMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CloudServerVO.CloudServer>> queryCloudServerPage(@Valid CloudServerParam.CloudServerPageQuery pageQuery) {
        return new HttpResult<>(cloudServerFacade.queryCloudServerPage(pageQuery));
    }

    @ApiOperation(value = "删除指定的云主机")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCloudServerById(@RequestParam int id) {
        return new HttpResult<>(cloudServerFacade.deleteCloudServerById(id));
    }

    @ApiOperation(value = "按类型同步云主机")
    @GetMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncCloudServerByKey(@RequestParam String key) {
        cloudServerFacade.syncCloudServerByKey(key);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "释放云主机")
    @PostMapping(value = "/delete/instance", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCloudServer(@RequestBody @Valid CloudServerParam.DeleteInstance param) {
        return new HttpResult<>(cloudServerFacade.deleteCloudServer(param));
    }

    @ApiOperation(value = "云主机变更计费方式")
    @PostMapping(value = "/charge/type/modify", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> modifyInstanceChargeType(@RequestBody @Valid CloudServerParam.ModifyInstanceChargeType param) {
        return new HttpResult<>(cloudServerFacade.modifyInstanceChargeType(param));
    }

    @ApiOperation(value = "云主机开机")
    @PostMapping(value = "/power/on", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> cloudServerPowerOn(@RequestBody @Valid CloudServerParam.PowerAction param) {
        return new HttpResult<>(cloudServerFacade.cloudServerPowerOn(param));
    }

    @ApiOperation(value = "云主机电源状态查询")
    @PostMapping(value = "/power/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Integer> queryPowerStatus(@RequestBody @Valid CloudServerParam.PowerAction param) {
        return new HttpResult<>(cloudServerFacade.queryPowerStatus(param));
    }

    @ApiOperation(value = "分页查询云主机付费列表")
    @PostMapping(value = "/charge/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CloudServerVO.CloudServer>> queryCloudServerChargePage(@RequestBody @Valid CloudServerParam.CloudServerChargePageQuery pageQuery) {
        return new HttpResult<>(cloudServerFacade.queryCloudServerChargePage(pageQuery));
    }

    @ApiOperation(value = "同步指定的云主机")
    @PutMapping(value = "/instance/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncCloudServerById(@RequestParam @Valid int id) {
        cloudServerFacade.syncCloudServerById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "云主机月新增统计")
    @GetMapping(value = "/month/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<CloudServerStatsVO.ServerMonthStats>> queryServerMonthStatsReport(@RequestParam Integer queryYear) {
        return new HttpResult<>(cloudServerFacade.queryServerMonthStatsReport(queryYear));
    }

    @ApiOperation(value = "云主机月新增根据类型统计")
    @PostMapping(value = "/type/month/stats", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<CloudServerStatsVO.ServerMonthStatsByType>> queryServerMonthStatsReportByType(@RequestBody CloudServerStatsParam.MonthStats param) {
        return new HttpResult<>(cloudServerFacade.queryServerMonthStatsReportByType(param));
    }

    @ApiOperation(value = "云主机资源统计")
    @GetMapping(value = "/res/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<CloudServerStatsVO.ServerResStats> queryServerResStatsReport(@RequestParam Integer cloudServerType) {
        return new HttpResult<>(cloudServerFacade.queryServerResStatsReport(cloudServerType));
    }
}
