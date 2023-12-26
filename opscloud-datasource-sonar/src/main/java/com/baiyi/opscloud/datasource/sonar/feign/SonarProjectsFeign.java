package com.baiyi.opscloud.datasource.sonar.feign;

import com.baiyi.opscloud.datasource.sonar.entity.SonarProjects;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/10/22 4:28 下午
 * @Version 1.0
 */
public interface SonarProjectsFeign {

    /**
     * https://docs.sonarqube.org/7.9/extend/web-api/
     *
     * User Token
     * This is the recommended way. Benefits are described in the page User Token. The token is sent via the login field of HTTP basic authentication, without any password.
     *
     * # note that the colon after the token is required in curl to set an empty password
     * curl -u THIS_IS_MY_TOKEN: https://sonarqube.com/api/user_tokens/search
     * HTTP Basic Access
     * Login and password are sent via the standard HTTP Basic fields:
     *
     * curl -u MY_LOGIN:MY_PASSWORD https://sonarqube.com/api/user_tokens/search
     * Users who authenticate in web application through an OAuth provider, for instance GitHub or Bitbucket, don't have credentials and can't use HTTP Basic mode. They must generate and use tokens.
     *
     *
     * @param authBasic
     * @param paramMap
     * @return
     */
    @RequestLine("GET /api/projects/search")
    @Headers({"Content-Type: application/json;charset=utf-8",
            "Authorization: Basic {authBasic}"})
    SonarProjects searchProjects(@Param("authBasic") String authBasic, @QueryMap Map<String, String> paramMap);

}