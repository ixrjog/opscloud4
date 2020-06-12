package com.baiyi.opscloud.controller;


import com.baiyi.opscloud.account.AccountCenter;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.auth.LogParam;
import com.baiyi.opscloud.domain.vo.auth.LogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author feixue
 */
@RestController
@RequestMapping("/log")
@Api(tags = "用户登录")
public class LogController {

    @Resource
    private AccountCenter accountCenter;

    /**
     * 用户登录接口
     *
     * @param loginParam
     * @return
     */
    @ApiOperation(value = "用户登录接口")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LogVO.LoginVO> login(@RequestBody LogParam.LoginParam loginParam) {
        return new HttpResult<>(accountCenter.loginCheck(loginParam));
    }
}
