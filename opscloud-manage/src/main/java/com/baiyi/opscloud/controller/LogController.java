package com.baiyi.opscloud.controller;


import com.baiyi.opscloud.account.AccountCenter;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.auth.LogParam;
import com.baiyi.opscloud.domain.vo.auth.LogVO;
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
public class LogController {

    @Resource
    private AccountCenter accountCenter;

    /**
     * 用户登录接口
     *
     * @param loginParam
     * @return
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LogVO.LoginVO> login(@RequestBody LogParam.LoginParam loginParam) {
        return new HttpResult<>(accountCenter.loginCheck(loginParam));
    }
}
