package com.baiyi.opscloud.controller.socket;

import com.baiyi.opscloud.controller.socket.base.SimpleAuthentication;
import com.baiyi.opscloud.terminal.audit.ITerminalAuditHandler;
import com.baiyi.opscloud.terminal.audit.TerminalAuditHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

/**
 * @Author baiyi
 * @Date 2021/7/23 2:39 下午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/terminal/session/audit")
@Component
public class TerminalSessionAuditController extends SimpleAuthentication {

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        session.setMaxIdleTimeout(ServerTerminalController.WEBSOCKET_TIMEOUT);
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
        String state = getState(message);
        ITerminalAuditHandler terminalAuditHandler = TerminalAuditHandlerFactory.getHandlerByKey(state);
        if (terminalAuditHandler != null) {
            terminalAuditHandler.handle(message, session);
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