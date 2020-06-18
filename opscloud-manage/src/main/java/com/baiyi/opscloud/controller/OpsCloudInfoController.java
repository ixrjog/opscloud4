package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.HttpResult;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;


/**
 * @Author baiyi
 * @Date 2019/12/25 6:07 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/opscloud")
@Api(tags = "opscloudInfo")
public class OpsCloudInfoController {


    @ApiOperation(value = "opscloud版本详情")
    @GetMapping(value = "/info")
    public HttpResult<Boolean> getOpscloudInfo() {
        return HttpResult.SUCCESS;
    }

//    @ApiOperation(value = "opscloud版本详情")
//    @PostMapping(value = "/info", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public HttpResult<Boolean> getOpscloudInfo() {
//        return HttpResult.SUCCESS;
//    }


}
