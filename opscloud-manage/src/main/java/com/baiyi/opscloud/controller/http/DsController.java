package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.datasource.DsConfigParam;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import com.baiyi.opscloud.domain.vo.datasource.DsConfigVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.facade.datasource.DsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:30 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/datasource")
@Tag(name = "数据源")
@RequiredArgsConstructor
public class DsController {

    private final DsFacade datasourceFacade;

    @Operation(summary = "查询数据源配置类型选项")
    @GetMapping(value = "/config/type/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getDsConfigTypeOptions() {
        return new HttpResult<>(DsTypeEnum.toOptions());
    }

    @Operation(summary = "分页查询数据源配置")
    @PostMapping(value = "/config/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DsConfigVO.DsConfig>> queryDsConfigPage(@RequestBody @Valid DsConfigParam.DsConfigPageQuery pageQuery) {
        return new HttpResult<>(datasourceFacade.queryDsConfigPage(pageQuery));
    }

    @Operation(summary = "id查询数据源配置")
    @GetMapping(value = "/config/id/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DsConfigVO.DsConfig> queryDsConfigById(@RequestParam int configId) {
        return new HttpResult<>(datasourceFacade.queryDsConfigById(configId));
    }

    @Operation(summary = "新增数据源配置")
    @PostMapping(value = "/config/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addDsConfig(@RequestBody @Valid DsInstanceParam.AddDsConfig dsConfig) {
        datasourceFacade.addDsConfig(dsConfig);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新数据源配置")
    @PutMapping(value = "/config/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateDsConfig(@RequestBody @Valid DsInstanceParam.UpdateDsConfig dsConfig) {
        datasourceFacade.updateDsConfig(dsConfig);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "设置实例配置")
    @PutMapping(value = "/instance/config/set", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setDsInstanceConfig(@RequestParam int instanceId) {
        datasourceFacade.setDsInstanceConfig(instanceId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "注册数据源实例")
    @PostMapping(value = "/instance/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> registerDsInstance(@RequestBody @Valid DsInstanceParam.RegisterDsInstance registerDsInstance) {
        datasourceFacade.registerDsInstance(registerDsInstance);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "查询数据源实例")
    @PostMapping(value = "/instance/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<DsInstanceVO.Instance>> queryDsInstance(@RequestBody @Valid DsInstanceParam.DsInstanceQuery query) {
        return new HttpResult<>(datasourceFacade.queryDsInstance(query));
    }

    @Operation(summary = "查询指定ID的数据源实例")
    @GetMapping(value = "/instance/id/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DsInstanceVO.Instance> queryDsInstanceById(@RequestParam int instanceId) {
        return new HttpResult<>(datasourceFacade.queryDsInstanceById(instanceId));
    }

}