package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.facade.DubboMappingFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/10/27 4:50 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/dubbo/mapping")
@Api(tags = "文档管理")
public class DubboMappingController {

    @Resource
    private DubboMappingFacade dubboMappingFacade;

    @ApiOperation(value = "刷新Dubbo映射配置")
    @GetMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> refreshDubboMapping(@Valid String env) {
        dubboMappingFacade.refreshDubboProviderByEnv(env);
        return HttpResult.SUCCESS;
    }

}
