package com.baiyi.opscloud.datasource.jenkins.feign;

import com.baiyi.opscloud.datasource.jenkins.entity.JenkinsUser;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/5 10:36 AM
 * @Version 1.0
 */
public interface JenkinsUserFeign {

    @RequestLine("GET /blue/rest/organizations/jenkins/users/{username}")
    @Headers({"Content-Type: " + MediaType.APPLICATION_JSON_UTF8_VALUE, "Authorization: Basic {authBasic}"})
    JenkinsUser.User getUser(@Param("authBasic") String authBasic, @Param("username") String username);


    @RequestLine("GET /blue/rest/organizations/jenkins/users/")
    @Headers({"Content-Type: " + MediaType.APPLICATION_JSON_UTF8_VALUE, "Authorization: Basic {authBasic}"})
    List<JenkinsUser.User> listUsers(@Param("authBasic") String authBasic);

}
