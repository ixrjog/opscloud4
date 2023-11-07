package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.auth.AuthPlatformParam;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.baiyi.opscloud.domain.vo.auth.AuthPlatformVO;
import com.baiyi.opscloud.domain.vo.auth.LogVO;
import com.baiyi.opscloud.facade.auth.AuthPlatformFacade;
import com.baiyi.opscloud.facade.auth.UserAuthFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/11 11:49 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/log")
@Tag(name = "用户登录")
@RequiredArgsConstructor
public class LogController {

    private final UserAuthFacade userAuthFacade;

    private final AuthPlatformFacade authPlatformFacade;

    @Operation(summary = "用户登录接口")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LogVO.Login> login(@RequestBody LoginParam.Login loginParam) {
        return new HttpResult<>(userAuthFacade.login(loginParam));
    }

    @Operation(summary = "用户登出接口")
    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> logout() {
        userAuthFacade.logout();
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "外部平台认证使用不返回Token")
    @PostMapping(value = "/platform/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LogVO.Login> platformLogin(@RequestBody LoginParam.PlatformLogin loginParam) {
        return new HttpResult<>(userAuthFacade.platformLogin(loginParam));
    }

    @Operation(summary = "平台认证选项")
    @GetMapping(value = "/platform/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<AuthPlatformVO.Platform>> getPlatformOptions() {
        return new HttpResult<>(authPlatformFacade.getPlatformOptions());
    }

    @Operation(summary = "平台认证日志分页查询")
    @PostMapping(value = "/platform/auth/log/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AuthPlatformVO.AuthPlatformLog>> queryAuthPlatformLogPage(@RequestBody AuthPlatformParam.AuthPlatformLogPageQuery pageQuery) {
        return new HttpResult<>(authPlatformFacade.queryAuthPlatformLog(pageQuery));
    }

}