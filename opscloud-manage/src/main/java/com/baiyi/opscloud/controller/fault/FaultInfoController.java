package com.baiyi.opscloud.controller.fault;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultRootCauseType;
import com.baiyi.opscloud.domain.param.fault.FaultParam;
import com.baiyi.opscloud.domain.vo.fault.FaultVO;
import com.baiyi.opscloud.facade.fault.FaultInfoFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/7 10:32 上午
 * @Since 1.0
 */

@RestController
@RequestMapping("/fault/info")
@Api(tags = "故障管理")
public class FaultInfoController {

    @Resource
    private FaultInfoFacade faultInfoFacade;

    @ApiOperation(value = "故障信息分页查询")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<FaultVO.FaultInfo>> queryFaultInfoPage(@RequestBody FaultParam.InfoPageQuery pageQuery) {
        return new HttpResult<>(faultInfoFacade.queryFaultInfoPage(pageQuery));
    }

    @ApiOperation(value = "故障Action分页查询")
    @PostMapping(value = "/action/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<FaultVO.FaultAction>> queryFaultActionPage(@RequestBody FaultParam.ActionPageQuery pageQuery) {
        return new HttpResult<>(faultInfoFacade.queryFaultActionPage(pageQuery));
    }

    @ApiOperation(value = "保存故障信息")
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveFaultInfo(@RequestBody @Valid FaultVO.FaultInfo faultInfo) {
        return new HttpResult<>(faultInfoFacade.saveFaultInfo(faultInfo));
    }

    @ApiOperation(value = "新增原因归类")
    @PostMapping(value = "/cause/type/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRootCauseType(@RequestBody @Valid FaultParam.AddRootCauseType param) {
        return new HttpResult<>(faultInfoFacade.addRootCauseType(param));
    }

    @ApiOperation(value = "更新故障action")
    @PostMapping(value = "/action/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateFaultAction(@RequestBody @Valid FaultVO.FaultAction faultAction) {
        return new HttpResult<>(faultInfoFacade.updateFaultAction(faultAction));
    }

    @ApiOperation(value = "删除故障信息")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delFaultInfo(@RequestParam Integer id) {
        return new HttpResult<>(faultInfoFacade.delFaultInfo(id));
    }

    @ApiOperation(value = "查询故障原因归类")
    @PostMapping(value = "/cause/type/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcFaultRootCauseType>> queryFaultRootCauseTypePage(@RequestBody FaultParam.RootCauseTypePageQuery pageQuery) {
        return new HttpResult<>(faultInfoFacade.queryFaultRootCauseTypePage(pageQuery));
    }

    @ApiOperation(value = "更新故障是否完成")
    @GetMapping(value = "/finalized/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateFaultInfoFinalized(@RequestParam Integer id) {
        return new HttpResult<>(faultInfoFacade.updateFaultInfoFinalized(id));
    }

    @ApiOperation(value = "查询故障信息", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/query")
    public HttpResult<FaultVO.FaultInfo> queryFaultInfo(@RequestParam Integer id) {
        return new HttpResult<>(faultInfoFacade.queryFaultInfo(id));
    }

    @ApiOperation(value = "通过年份查询月故障统计")
    @GetMapping(value = "/month/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<FaultVO.InfoMonthStats> queryFaultInfoMonthStats(@RequestParam Integer year) {
        return new HttpResult<>(faultInfoFacade.queryFaultInfoMonthStats(year));
    }
}
