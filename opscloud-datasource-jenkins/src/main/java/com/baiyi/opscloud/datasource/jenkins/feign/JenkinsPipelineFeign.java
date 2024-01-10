package com.baiyi.opscloud.datasource.jenkins.feign;

import com.baiyi.opscloud.datasource.jenkins.entity.JenkinsUser;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/5 1:36 PM
 * @Version 1.0
 */
public interface JenkinsPipelineFeign {

    @RequestLine("GET /blue/rest/organizations/jenkins/pipelines/")
    @Headers({"Content-Type: application/json;charset=UTF-8", "Authorization: Basic {authBasic}"})
    List<JenkinsUser.User> listPipelines(@Param("authBasic") String authBasic);

}