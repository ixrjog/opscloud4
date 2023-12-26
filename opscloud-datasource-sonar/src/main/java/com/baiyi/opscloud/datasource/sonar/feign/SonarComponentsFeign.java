package com.baiyi.opscloud.datasource.sonar.feign;

import com.baiyi.opscloud.datasource.sonar.entity.SonarComponents;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/10/22 3:58 下午
 * @Version 1.0
 */
public interface SonarComponentsFeign {

    @RequestLine("GET /api/components/search")
    @Headers({"Content-Type: application/json;charset=utf-8",
            "Authorization: Basic {authBasic}"})
    SonarComponents searchComponents(@Param("authBasic") String authBasic, @QueryMap Map<String, String> paramMap);

}