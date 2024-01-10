package com.baiyi.opscloud.datasource.metersphere.feign;

import com.baiyi.opscloud.datasource.metersphere.entity.LeoHookResult;
import com.baiyi.opscloud.domain.hook.leo.LeoHook;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2023/5/15 13:42
 * @Version 1.0
 */
public interface MeterSphereHookV1Feign {

    @RequestLine("POST {path}")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    LeoHookResult.Result sendBuildHook(@Param("path") String path, LeoHook.BuildHook hook);

    @RequestLine("POST {path}")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    LeoHookResult.Result sendDeployHook(@Param("path") String path, LeoHook.DeployHook hook);

}