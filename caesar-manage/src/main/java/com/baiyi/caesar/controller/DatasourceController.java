package com.baiyi.caesar.controller;

import com.baiyi.caesar.common.HttpResult;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.datasource.DatasourceConfigParam;
import com.baiyi.caesar.domain.param.datasource.DatasourceInstanceParam;
import com.baiyi.caesar.facade.DatasourceFacade;
import com.baiyi.caesar.types.DatasourceTypeEnum;
import com.baiyi.caesar.vo.common.OptionsVO;
import com.baiyi.caesar.vo.datasource.DatasourceConfigVO;
import com.baiyi.caesar.vo.datasource.DatasourceInstanceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:30 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/datasource")
@Api(tags = "用户管理")
public class DatasourceController {

    @Resource
    private DatasourceFacade datasourceFacade;

    @ApiOperation(value = "查询数据源配置类型选项")
    @GetMapping(value = "/config/type/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getDsConfigTypeOptions() {
        return new HttpResult<>(DatasourceTypeEnum.toOptions());
    }

    @ApiOperation(value = "分页查询数据源配置")
    @PostMapping(value = "/config/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DatasourceConfigVO.DsConfig>> queryDsConfigPage(@RequestBody @Valid DatasourceConfigParam.DatasourceConfigPageQuery pageQuery) {
        return new HttpResult<>(datasourceFacade.queryDsConfigPage(pageQuery));
    }

    @ApiOperation(value = "新增数据源配置")
    @PostMapping(value = "/config/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addDsConfig(@RequestBody @Valid DatasourceConfigVO.DsConfig dsConfig) {
        datasourceFacade.addDsConfig(dsConfig);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新数据源配置")
    @PutMapping(value = "/config/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateDsConfig(@RequestBody @Valid DatasourceConfigVO.DsConfig dsConfig) {
        datasourceFacade.updateDsConfig(dsConfig);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "注册数据源实例")
    @PostMapping(value = "/instance/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> registerDsInstance(@RequestBody @Valid DatasourceInstanceParam.RegisterDsInstance registerDsInstance) {
        datasourceFacade.registerDsInstance(registerDsInstance);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "查询数据源实例")
    @PostMapping(value = "/instance/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<DatasourceInstanceVO.Instance>> queryDsInstance(@RequestBody @Valid DatasourceInstanceParam.DsInstanceQuery query) {
        return new HttpResult<>(datasourceFacade.queryDsInstance(query));
    }

}
