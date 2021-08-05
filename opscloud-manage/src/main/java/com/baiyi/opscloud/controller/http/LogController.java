package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.baiyi.opscloud.facade.auth.UserAuthFacade;
import com.baiyi.opscloud.domain.vo.auth.LogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/11 11:49 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/log")
@Api(tags = "用户登录")
public class LogController {

    @Resource
    private UserAuthFacade userAuthFacade;

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

    /**
     * 用户登出接口
     *
     * @return
     */
    @ApiOperation(value = "用户登出接口")
    @PutMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> logout() {
        userAuthFacade.logout();
        return HttpResult.SUCCESS;
    }
}
