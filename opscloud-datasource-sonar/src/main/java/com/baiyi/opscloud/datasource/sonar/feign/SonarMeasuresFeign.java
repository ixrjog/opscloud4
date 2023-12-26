package com.baiyi.opscloud.datasource.sonar.feign;

import com.baiyi.opscloud.datasource.sonar.entity.SonarMeasures;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/10/22 2:33 下午
 * @Version 1.0
 */
public interface SonarMeasuresFeign {

    @RequestLine("GET /api/measures/component")
    @Headers({"Content-Type: application/json;charset=utf-8",
            "Authorization: Basic {authBasic}"})
    SonarMeasures queryMeasuresComponent(@Param("authBasic") String authBasic, @QueryMap Map<String, String> paramMap);

}