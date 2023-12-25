package com.baiyi.opscloud.leo.message.handler.base;

import com.baiyi.opscloud.domain.param.leo.request.ILeoRequestParam;

import jakarta.websocket.Session;
import java.io.IOException;


/**
 * @Author baiyi
 * @Date 2022/11/23 15:51
 * @Version 1.0
 */
public interface ILeoContinuousDeliveryRequestHandler extends ILeoRequestParam {

    /**
     * 请求接口
     * @param sessionId
     * @param session
     * @param message
     * @throws IOException
     */
    void handleRequest(String sessionId, Session session, String message) throws IOException;

}