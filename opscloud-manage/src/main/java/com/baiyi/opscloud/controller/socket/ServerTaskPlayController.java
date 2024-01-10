package com.baiyi.opscloud.controller.socket;

import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.controller.socket.base.SimpleAuthentication;
import com.baiyi.opscloud.datasource.ansible.play.ITaskPlayProcessor;
import com.baiyi.opscloud.datasource.ansible.play.ServerTaskPlayFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

/**
 * @Author baiyi
 * @Date 2021/9/26 3:33 下午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/server/task/play")
@Component
public class ServerTaskPlayController extends SimpleAuthentication {

    /**
     * 超时时间1H
     */
    public static final long WEBSOCKET_TIMEOUT = NewTimeUtil.HOUR_TIME;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        // sessionSet.get().remove(session);
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
        String state = getState(message);
        ITaskPlayProcessor iTaskPlayProcess = ServerTaskPlayFactory.getProcessByKey(state);
        if (iTaskPlayProcess != null) {
            iTaskPlayProcess.process(message, session);
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