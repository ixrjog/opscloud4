package com.baiyi.opscloud.datasource.aliyun.eventbridge.feign;

import com.baiyi.opscloud.datasource.aliyun.eventbridge.entity.AliyunEventBridgeResult;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.entity.CloudEvents;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2023/8/30 19:53
 * @Version 1.0
 */
public interface AliyunEventBridgeHookFeign {

    /**
     * https://eventbridge.console.aliyun.com/eu-central-1/event-bus/Event_Hub_Bridge/event-source/event.application.publish/detail
     * @param path
     * @param token
     * @param event
     * @return
     */
    @RequestLine("POST {path}?token={token}")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    AliyunEventBridgeResult.Result publish(@Param("path") String path, @Param("token") String token, CloudEvents.Event event);

}