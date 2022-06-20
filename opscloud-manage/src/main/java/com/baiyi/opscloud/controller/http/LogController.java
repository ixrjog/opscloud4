package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.baiyi.opscloud.domain.vo.auth.LogVO;
import com.baiyi.opscloud.facade.auth.UserAuthFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/5/11 11:49 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/log")
@Api(tags = "用户登录")
@RequiredArgsConstructor
public class LogController {

    private final UserAuthFacade userAuthFacade;

    /**
     * 用户登录接口
     *
     * @param loginParam
     * @return
     */
    @ApiOperation(value = "用户登录接口")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LogVO.Login> login(@RequestBody LoginParam.Login loginParam) {
        return new HttpResult<>(userAuthFacade.login(loginParam));
    }

    @ApiOperation(value = "用户登录接口（外部认证使用不返回Token）")
    @PostMapping(value = "/simple/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LogVO.Login> simpleLogin(@RequestBody LoginParam.Login loginParam) {
        return new HttpResult<>(userAuthFacade.simpleLogin(loginParam));
    }


    @ApiOperation(value = "用户登出接口")
    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> logout() {
        userAuthFacade.logout();
        return HttpResult.SUCCESS;
    }

}
