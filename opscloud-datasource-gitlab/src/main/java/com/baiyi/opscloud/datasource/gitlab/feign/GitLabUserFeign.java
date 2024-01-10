package com.baiyi.opscloud.datasource.gitlab.feign;

import feign.RequestLine;
import feign.Response;

/**
 * @Author baiyi
 * @Date 2022/10/31 10:59
 * @Version 1.0
 */
public interface GitLabUserFeign {

    /**
     * https://static-legacy.dingtalk.com/media/lQDPDhvVtvtbMfPNA8LNA8CwHt7eHxEhYoQDLtLPsEC3AA_960_962.jpg
     * @return
     */
    @RequestLine("GET /media/{fileName}")
    Response getUserAvatar();

}