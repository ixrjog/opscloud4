package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.facade.SettingFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/6/4 5:33 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/setting")
@Api(tags = "全局设置")
public class SettingController {

    @Resource
    private SettingFacade settingFacade;

    @ApiOperation(value = "查询全局配置")
    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<String, String>> queryServerGroupProperty(@RequestParam String name) {
        return new HttpResult<>(settingFacade.querySettingMap(name));
    }

}
