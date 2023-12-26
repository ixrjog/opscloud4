package com.baiyi.opscloud.datasource.dingtalk.feign;

import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkToken;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2021/11/29 2:56 下午
 * @Version 1.0
 */
public interface DingtalkTokenFeign {

    @RequestLine("GET /gettoken?" +
            "appkey={appkey}" +
            "&appsecret={appsecret}")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    DingtalkToken.TokenResponse getToken(@Param("appkey") String appkey, @Param("appsecret") String appsecret);

}