package com.baiyi.opscloud.datasource.nacos.feign;

import com.baiyi.opscloud.datasource.nacos.entry.NacosLogin;
import com.baiyi.opscloud.datasource.nacos.entry.NacosPermission;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/11/11 4:17 下午
 * @Version 1.0
 */
public interface NacosAuthV1Feign {

    @RequestLine("POST /nacos/v1/auth/users/login")
    @Headers({"content-type: application/x-www-form-urlencoded"})
    NacosLogin.AccessToken login(@QueryMap Map<String, String> loginParam);

    @RequestLine("GET /nacos/v1/auth/permissions?" +
            "pageNo={pageNo}" +
            "&pageSize={pageSize}" +
            "&accessToken={accessToken}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    NacosPermission.PermissionsResponse listPermissions(@Param("pageNo") Integer pageNo,
                                                        @Param("pageSize") Integer pageSize,
                                                        @Param("accessToken") String accessToken
    );

}
