package com.baiyi.opscloud.datasource.dingtalk.feign;

import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkUser;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkUserParam;
import feign.*;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/11/29 4:07 下午
 * @Version 1.0
 */
public interface DingtalkUserFeign {

    @RequestLine("POST /topapi/v2/user/list?access_token={accessToken}")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    DingtalkUser.UserResponse list(@Param("accessToken") String accessToken, DingtalkUserParam.QueryUserPage queryUserPage);

    /**
     * https://static-legacy.dingtalk.com/media/lQDPDhvVtvtbMfPNA8LNA8CwHt7eHxEhYoQDLtLPsEC3AA_960_962.jpg
     * @return
     */
    @RequestLine("GET {path}")
    Response getAvatar(@QueryMap Map<String, String> queryMap);

}