package com.baiyi.opscloud.datasource.sonar.feign;

import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/21 下午4:37
 * &#064;Version 1.0
 */
public interface SonarProjectTagsFeign {

    @RequestLine("POST api/project_tags/set")
    @Headers({"Content-Type: application/json;charset=utf-8",
            "Authorization: Basic {authBasic}"})
    void setProjectTags(@Param("authBasic") String authBasic, @QueryMap Map<String, String> paramMap);

}
