package com.baiyi.opscloud.controller.tencent;

import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.tencent.ExmailParam;
import com.baiyi.opscloud.facade.tencent.TencentExmailUserFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 4:05 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/tencent/exmail")
@Api(tags = "腾讯企业邮箱管理")
public class TencentExmailController {

    @Resource
    private TencentExmailUserFacade tencentExmailUserFacade;

    @ApiOperation(value = "用户创建")
    @PostMapping(value = "/user/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createUser(@RequestBody @Valid ExmailParam.User param) {
        return new HttpResult<>(tencentExmailUserFacade.createUser(param));
    }

    @ApiOperation(value = "用户校验")
    @GetMapping(value = "/user/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> checkUser(@RequestParam String userId) {
        return new HttpResult<>(tencentExmailUserFacade.checkUser(userId));
    }

}
