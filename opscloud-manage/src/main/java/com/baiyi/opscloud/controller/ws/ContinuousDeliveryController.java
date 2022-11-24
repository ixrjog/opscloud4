package com.baiyi.opscloud.controller.ws;

import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.controller.ws.base.SimpleAuthentication;
import com.baiyi.opscloud.domain.param.leo.request.ILeoRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.LoginLeoRequestParam;
import com.baiyi.opscloud.leo.message.handler.base.ILeoContinuousDeliveryRequestHandler;
import com.baiyi.opscloud.leo.message.factory.LeoContinuousDeliveryMessageHandlerFactory;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author baiyi
 * @Date 2022/11/23 15:17
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/continuous/delivery")
@Component
public class ContinuousDeliveryController extends SimpleAuthentication {

    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    private static final ThreadLocal<CopyOnWriteArraySet<Session>> sessionSet = ThreadLocal.withInitial(CopyOnWriteArraySet::new);

    private String loginUsername;

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
    }

    /**
     * 收到客户端消息后调用的方法
     * Session session
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage(maxMessageSize = 512 * 1024)
    public void onMessage(String message, Session session) {
        if (!session.isOpen() || StringUtils.isEmpty(message)) return;
        String messageType = getLeoMessageType(message);
        // 处理登录状态
        if (StringUtils.isEmpty(this.loginUsername)) {
            // 鉴权并更新会话信息
            if (LeoRequestType.LOGIN.name().equals(messageType))
                hasLogin(new GsonBuilder().create().fromJson(message, LoginLeoRequestParam.class));
        } else {
            SessionUtil.setUsername(this.loginUsername);
        }
        ILeoContinuousDeliveryRequestHandler iMessageHandler = LeoContinuousDeliveryMessageHandlerFactory.getHandlerByMessageType(messageType);
        if (iMessageHandler != null) {
            iMessageHandler.handleRequest(session, message);
        } else {
            log.warn("ContinuousDelivery消息类型不存在: messageType={}", messageType);
        }
    }

    protected String getLeoMessageType(String message) {
        ILeoRequestParam im = new GsonBuilder().create().fromJson(message, ILeoRequestParam.class);
        return im.getMessageType();
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
