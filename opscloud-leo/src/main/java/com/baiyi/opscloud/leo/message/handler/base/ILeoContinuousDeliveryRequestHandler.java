package com.baiyi.opscloud.leo.message.handler.base;

import com.baiyi.opscloud.domain.param.leo.request.ILeoRequestParam;

import javax.websocket.Session;
import java.io.IOException;


/**
 * @Author baiyi
 * @Date 2022/11/23 15:51
 * @Version 1.0
 */
public interface ILeoContinuousDeliveryRequestHandler extends ILeoRequestParam {

    void handleRequest(String sessionId, Session session, String message) throws IOException;

}
