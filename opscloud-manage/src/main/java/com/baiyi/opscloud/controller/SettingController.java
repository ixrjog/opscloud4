package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.setting.SettingParam;
import com.baiyi.opscloud.facade.SettingBaseFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private SettingBaseFacade settingFacade;

    @ApiOperation(value = "查询全局配置")
    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<String, String>> querySettingMap(@RequestParam String name) {
        return new HttpResult<>(settingFacade.querySettingMap(name));
    }

    @ApiOperation(value = "查询全局配置")
    @GetMapping(value = "/all/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<String, String>> querySettingMap() {
        return new HttpResult<>(settingFacade.querySettingMap());
    }

    @ApiOperation(value = "更新全局配置")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateSetting(@RequestBody @Validated SettingParam.UpdateSettingParam updateSettingParam) {
        return new HttpResult<>(settingFacade.updateSetting(updateSettingParam));
    }

}
