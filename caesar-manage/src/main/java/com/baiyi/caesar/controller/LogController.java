package com.baiyi.caesar.controller;

import com.baiyi.caesar.common.HttpResult;
import com.baiyi.caesar.domain.param.auth.LoginParam;
import com.baiyi.caesar.facade.auth.UserAuthFacade;
import com.baiyi.caesar.domain.vo.auth.LogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
