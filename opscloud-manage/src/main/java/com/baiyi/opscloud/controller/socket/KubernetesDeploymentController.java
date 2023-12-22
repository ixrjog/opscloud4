package com.baiyi.opscloud.controller.socket;

import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.ws.KubernetesDeploymentQuerySessionMap;
import com.baiyi.opscloud.controller.socket.base.SimpleAuthentication;
import com.baiyi.opscloud.loop.KubernetesDeploymentEventLoop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.util.UUID;

/**
 * 容器堡垒机，应用纬度获取无状态详情
 *
 * @Author baiyi
 * @Date 2023/3/20 10:37
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/kubernetes/deployment")
@Component
public class KubernetesDeploymentController extends SimpleAuthentication {

    /**
     * 当前会话 UUID
     */
    private final String sessionId = UUID.randomUUID().toString();

    private String username;

    /**
     * 超时时间1H
     */
    public static final Long WEBSOCKET_TIMEOUT = NewTimeUtil.MINUTE_TIME * 5;

    private static ThreadPoolTaskExecutor xTerminalExecutor;

    @Resource
    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor xTerminalExecutor) {
        KubernetesDeploymentController.xTerminalExecutor = xTerminalExecutor;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
            xTerminalExecutor.execute(new KubernetesDeploymentEventLoop(sessionId, session));
        } catch (Exception e) {
            log.error("Kubernetes deployment connection error: {}", e.getMessage());
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
    }

    /**
     * 收到客户端消息后调用的方法
     * Session session
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage(maxMessageSize = 2 * 1024)
    public void onMessage(String message, Session session) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
//        if (StringUtils.isEmpty(this.username)) {
//            // 鉴权并更新会话信息
//            hasLogin(new GsonBuilder().create().fromJson(message, KubernetesDeploymentMessage.class));
//        } else {
//            SessionUtil.setUsername(this.username);
//        }
        KubernetesDeploymentQuerySessionMap.addSessionQueryMap(sessionId, message);
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