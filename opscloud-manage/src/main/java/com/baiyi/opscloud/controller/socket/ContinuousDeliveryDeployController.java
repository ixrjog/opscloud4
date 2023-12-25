package com.baiyi.opscloud.controller.socket;

import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.common.leo.response.LeoContinuousDeliveryResponse;
import com.baiyi.opscloud.common.leo.session.LeoDeployQuerySessionMap;
import com.baiyi.opscloud.controller.socket.base.SimpleAuthentication;
import com.baiyi.opscloud.domain.param.leo.request.LoginLeoRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.SimpleLeoRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import com.baiyi.opscloud.leo.task.loop.LeoDeployEventLoop;
import com.google.gson.GsonBuilder;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

import static com.baiyi.opscloud.controller.socket.ServerTerminalController.WEBSOCKET_TIMEOUT;

/**
 * @Author baiyi
 * @Date 2022/12/2 14:30
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/continuous-delivery/deploy")
@Component
public class ContinuousDeliveryDeployController extends SimpleAuthentication {

    /**
     * 当前会话UUID
     */
    private final String sessionId = UUID.randomUUID().toString();

    private String username;

//    private static ThreadPoolTaskExecutor leoExecutor;
//
//    @Autowired
//    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor leoExecutor) {
//        ContinuousDeliveryDeployController.leoExecutor = leoExecutor;
//    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
            //leoExecutor.execute(new LeoDeployEventLoop(this.sessionId, session));
            // JDK21 VirtualThreads
            Thread.ofVirtual().start(new LeoDeployEventLoop(this.sessionId, session));
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
                LeoContinuousDeliveryResponse<String> response = LeoContinuousDeliveryResponse.<String>builder()
                        .body("")
                        .messageType(LeoRequestType.AUTHENTICATION_FAILURE.name())
                        .build();
                sendToSession(session, response);
                return;
            }
        } else {
            SessionHolder.setUsername(this.username);
        }
        LeoDeployQuerySessionMap.addSessionQueryMap(this.sessionId, messageType, message);
    }

    protected String getLeoMessageType(String message) {
        SimpleLeoRequestParam requestParam = new GsonBuilder().create().fromJson(message, SimpleLeoRequestParam.class);
        return requestParam.getMessageType();
    }

    protected void sendToSession(Session session, LeoContinuousDeliveryResponse<String> response) {
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
    }

}