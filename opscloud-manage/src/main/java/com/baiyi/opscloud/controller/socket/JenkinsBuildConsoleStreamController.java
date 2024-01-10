package com.baiyi.opscloud.controller.socket;

import com.baiyi.opscloud.controller.socket.base.SimpleAuthentication;
import com.baiyi.opscloud.domain.param.leo.request.LoginLeoRequestParam;
import com.baiyi.opscloud.leo.message.factory.LeoContinuousDeliveryMessageHandlerFactory;
import com.baiyi.opscloud.leo.message.handler.base.ILeoContinuousDeliveryRequestHandler;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.UUID;

import static com.baiyi.opscloud.controller.socket.ServerTerminalController.WEBSOCKET_TIMEOUT;
import static com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType.QUERY_LEO_BUILD_CONSOLE_STREAM;

/**
 * 持续交付Jenkins console stream 流处理
 *
 * @Author baiyi
 * @Date 2022/11/30 11:07
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/continuous-delivery/jenkins/console/stream")
@Component
public class JenkinsBuildConsoleStreamController extends SimpleAuthentication {

    /**
     * 当前会话UUID
     */
    private final String sessionId = UUID.randomUUID().toString();

    private String username;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
        } catch (Exception e) {
            log.error("Create connection error: {}", e.getMessage());
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
    }

    /**
     * 收到客户端消息后调用的方法
     * Session session
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage(maxMessageSize = 10 * 1024)
    public void onMessage(String message, Session session) {
        if (!session.isOpen() || StringUtils.isEmpty(message)) {
            return;
        }
        //  QueryLeoBuildConsoleStreamRequestParam requestParam = new GsonBuilder().create().fromJson(message, QueryLeoBuildConsoleStreamRequestParam.class);
        // 处理登录状态
        hasLogin(new GsonBuilder().create().fromJson(message, LoginLeoRequestParam.class));
        ILeoContinuousDeliveryRequestHandler handler = LeoContinuousDeliveryMessageHandlerFactory.getHandlerByMessageType(QUERY_LEO_BUILD_CONSOLE_STREAM.name());
        if (handler != null) {
            try {
                handler.handleRequest(this.sessionId, session, message);
            } catch (IOException e) {
                log.debug(e.getMessage());
            }
        }
    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("会话错误: {}", error.getMessage());
    }

}