package com.baiyi.opscloud.controller.ws;

import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.common.leo.response.LeoContinuousDeliveryResponse;
import com.baiyi.opscloud.common.leo.session.LeoBuildQuerySessionMap;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.controller.ws.base.SimpleAuthentication;
import com.baiyi.opscloud.domain.param.leo.request.LoginLeoRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.SimpleLeoRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import com.baiyi.opscloud.leo.task.loop.LeoBuildEventLoop;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.UUID;

import static com.baiyi.opscloud.controller.ws.ServerTerminalController.WEBSOCKET_TIMEOUT;

/**
 * @Author baiyi
 * @Date 2022/11/23 15:17
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/continuous-delivery/build")
@Component
public class ContinuousDeliveryBuildController extends SimpleAuthentication {

    /**
     * 当前会话UUID
     */
    private final String sessionId = UUID.randomUUID().toString();

    private String username;

    private static ThreadPoolTaskExecutor leoExecutor;

    @Autowired
    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor leoExecutor) {
        ContinuousDeliveryBuildController.leoExecutor = leoExecutor;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
            // 线程池执行
            leoExecutor.execute(new LeoBuildEventLoop(this.sessionId, session));
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
    @OnMessage(maxMessageSize = 1024)
    public void onMessage(String message, Session session) {
        if (!session.isOpen() || StringUtils.isEmpty(message)) {
            return;
        }
        String messageType = getLeoMessageType(message);
        // 处理登录状态
        if (StringUtils.isEmpty(this.username)) {
            // 鉴权
            try {
                hasLogin(new GsonBuilder().create().fromJson(message, LoginLeoRequestParam.class));
            } catch (AuthenticationException e) {
                LeoContinuousDeliveryResponse response = LeoContinuousDeliveryResponse.builder()
                        .messageType(LeoRequestType.AUTHENTICATION_FAILURE.name())
                        .build();
                sendToSession(session, response);
                return;
            }
        } else {
            SessionUtil.setUsername(this.username);
        }
        LeoBuildQuerySessionMap.addSessionQueryMap(this.sessionId, messageType, message);
    }

    protected String getLeoMessageType(String message) {
        SimpleLeoRequestParam requestParam = new GsonBuilder().create().fromJson(message, SimpleLeoRequestParam.class);
        return requestParam.getMessageType();
    }

    protected void sendToSession(Session session, LeoContinuousDeliveryResponse response) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(response.toString());
            }
        } catch (IOException e) {
            log.warn(e.getMessage());
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
        // log.info("会话错误: err={}", error.getMessage());
    }

}
