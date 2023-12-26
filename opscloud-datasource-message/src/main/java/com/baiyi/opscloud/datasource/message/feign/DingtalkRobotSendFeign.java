package com.baiyi.opscloud.datasource.message.feign;

import com.baiyi.opscloud.datasource.message.DingtalkMsg;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2022/5/31 20:56
 * @Version 1.0
 */
public interface DingtalkRobotSendFeign {

    /**
     * 发送消息
     * @param token
     * @param msg
     * @return
     */
    @RequestLine("POST /robot/send?access_token={token}")
    @Headers({"Content-Type: application/json"})
    Object send(@Param("token") String token, DingtalkMsg.Msg msg);

}