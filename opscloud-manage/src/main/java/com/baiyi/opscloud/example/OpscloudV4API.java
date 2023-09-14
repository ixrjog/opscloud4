package com.baiyi.opscloud.example;


import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2023/9/14 15:50
 * @Version 1.0
 */
public interface OpscloudV4API {

    @RequestLine("POST /api/example/helloWorld")
    @Headers({"Content-Type: application/json", "AccessToken: {accessToken}"})
    Object helloWorld(@Param("accessToken") String accessToken);

}
