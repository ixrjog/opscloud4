package com.baiyi.opscloud.controller.prometheus;

import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.prometheus.PrometheusParam;
import com.baiyi.opscloud.facade.prometheus.PrometheusFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/2 4:36 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/prometheus")
@Api(tags = "Prometheus管理")
public class PrometheusController {

    @Resource
    private PrometheusFacade prometheusFacade;

    @ApiOperation(value = "创建配置文件")
    @GetMapping(value = "/config/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> prometheusConfigCreate() {
        return new HttpResult<>(prometheusFacade.createPrometheusConfigTask());
    }

    @ApiOperation(value = "预览配置文件")
    @GetMapping(value = "/config/preview", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<String> prometheusConfigPreview() {
        return new HttpResult<>(prometheusFacade.getPrometheusJobTemplate());
    }

    @ApiOperation(value = "预览组配置文件")
    @GetMapping(value = "/group/config/preview", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<String> prometheusGroupConfigPreview(@RequestParam Integer id) {
        return new HttpResult<>(prometheusFacade.getPrometheusJobTemplate(id));
    }

    @ApiOperation(value = "查询target")
    @GetMapping(value = "/group/target/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<String> getTargetMap(@RequestParam Integer id) {
        return new HttpResult<>(prometheusFacade.getGroupTarget(id));
    }

    @ApiOperation(value = "保存配置文件")
    @PostMapping(value = "/config/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> savePrometheusConfig(@RequestBody @Valid PrometheusParam.SaveConfig param) {
        return new HttpResult<>(prometheusFacade.savePrometheusConfig(param));
    }

    @ApiOperation(value = "查询配置文件")
    @GetMapping(value = "/config/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<String> queryPrometheusConfig() {
        return new HttpResult<>(prometheusFacade.queryPrometheusConfig());
    }

}
