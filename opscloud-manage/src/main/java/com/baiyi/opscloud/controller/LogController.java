package com.baiyi.opscloud.controller;


import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.LogParam;
import com.baiyi.opscloud.domain.vo.LogVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author feixue
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LogVO.LoginVO> login(@RequestBody LogParam.LoginParam loginParam) {
        LogVO.LoginVO loginVO = new LogVO.LoginVO();
        loginVO.setUuid(UUID.randomUUID().toString());
        loginVO.setToken(UUID.randomUUID().toString());
        return new HttpResult<>(loginVO);
    }
}
