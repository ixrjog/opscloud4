package com.baiyi.opscloud.datasource.dingtalk.feign;

import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkMessage;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkMessageParam;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2021/12/1 4:43 下午
 * @Version 1.0
 */
public interface DingtalkMessageFeign {

    /**
     * https://developers.dingtalk.com/document/app/asynchronous-sending-of-enterprise-session-messages
     * @param accessToken
     * @param asyncSendMessage
     * @return
     */
    @RequestLine("POST /topapi/message/corpconversation/asyncsend_v2?access_token={accessToken}")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    DingtalkMessage.MessageResponse asyncSend(@Param("accessToken") String accessToken, DingtalkMessageParam.AsyncSendMessage asyncSendMessage);

}