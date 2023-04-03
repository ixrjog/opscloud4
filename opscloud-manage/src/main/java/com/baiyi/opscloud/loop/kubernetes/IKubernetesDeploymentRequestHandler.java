package com.baiyi.opscloud.loop.kubernetes;

import org.springframework.beans.factory.InitializingBean;

import jakarta.websocket.Session;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2023/3/20 17:13
 * @Version 1.0
 */
public interface IKubernetesDeploymentRequestHandler extends InitializingBean {

    /**
     * 消息类型
     * @return
     */
    String getMessageType();

    /**
     * 请求接口
     * @param sessionId
     * @param session
     * @param message
     * @throws IOException
     */
    void handleRequest(String sessionId, Session session, String message) throws IOException;

}
