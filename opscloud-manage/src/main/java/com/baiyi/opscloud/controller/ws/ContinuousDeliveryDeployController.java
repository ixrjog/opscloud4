package com.baiyi.opscloud.controller.ws;

import com.baiyi.opscloud.common.leo.session.LeoDeployQuerySessionMap;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.controller.ws.base.SimpleAuthentication;
import com.baiyi.opscloud.domain.param.leo.request.LoginLeoRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.SimpleLeoRequestParam;
import com.baiyi.opscloud.leo.task.WatchLeoDeployTask;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import static com.baiyi.opscloud.controller.ws.ServerTerminalController.WEBSOCKET_TIMEOUT;

/**
 * @Author baiyi
 * @Date 2022/12/2 14:30
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/continuous-delivery/deploy")
@Component
public class ContinuousDeliveryDeployController extends SimpleAuthentication {

    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    private static final ThreadLocal<CopyOnWriteArraySet<Session>> sessionSet = ThreadLocal.withInitial(CopyOnWriteArraySet::new);

    // 当前会话ID
    private final String sessionId = UUID.randomUUID().toString();

    private String username;

    private Session session = null;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            sessionSet.get().add(session);
            int cnt = onlineCount.incrementAndGet(); // 在线数加1
            this.session = session;
            session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
            WatchLeoDeployTask watchLeoDeployTask = new WatchLeoDeployTask(this.sessionId, session);
            Thread thread = new Thread(watchLeoDeployTask);
            thread.start();
        } catch (Exception e) {
            log.error("Create connection error: {}", e.getMessage());
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            sessionSet.get().remove(session);
            int cnt = onlineCount.decrementAndGet();
        } catch (Exception e) {
        }
        log.info("会话关闭！");
    }

    /**
     * 收到客户端消息后调用的方法
     * Session session
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage(maxMessageSize = 512 * 1024)
    public void onMessage(String message, Session session) {
        log.info("message={}", message);
        if (!session.isOpen() || StringUtils.isEmpty(message)) return;
        String messageType = getLeoMessageType(message);
        // 处理登录状态
        if (StringUtils.isEmpty(this.username)) {
            // 鉴权
            hasLogin(new GsonBuilder().create().fromJson(message, LoginLeoRequestParam.class));
//            if (LeoRequestType.LOGIN.name().equals(messageType))
//                hasLogin(new GsonBuilder().create().fromJson(message, LoginLeoRequestParam.class));
        } else {
            SessionUtil.setUsername(this.username);
        }
        LeoDeployQuerySessionMap.addSessionQueryMap(this.sessionId, messageType, message);
    }

    protected String getLeoMessageType(String message) {
        SimpleLeoRequestParam requestParam = new GsonBuilder().create().fromJson(message, SimpleLeoRequestParam.class);
        return requestParam.getMessageType();
    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("会话错误: err={}", error.getMessage());
    }

}

