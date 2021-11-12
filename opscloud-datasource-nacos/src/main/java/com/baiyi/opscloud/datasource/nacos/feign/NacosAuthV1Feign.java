package com.baiyi.opscloud.datasource.nacos.feign;

import com.baiyi.opscloud.datasource.nacos.entry.NacosLogin;
import feign.Headers;
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

}
