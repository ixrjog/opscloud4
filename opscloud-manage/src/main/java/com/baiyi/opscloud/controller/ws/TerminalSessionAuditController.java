package com.baiyi.opscloud.controller.ws;

import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.controller.ws.base.SimpleAuthentication;
import com.baiyi.opscloud.terminal.audit.ITerminalAuditHandler;
import com.baiyi.opscloud.terminal.audit.TerminalAuditHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @Author baiyi
 * @Date 2021/7/23 2:39 下午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/terminal/session/audit")
@Component
public class TerminalSessionAuditController extends SimpleAuthentication {

    private Session session = null;
    // 超时时间1H
    public static final Long WEBSOCKET_TIMEOUT = TimeUtil.hourTime;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
        this.session = session;
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
    @OnMessage
    public void onMessage(String message, Session session) {
        if (!session.isOpen() || StringUtils.isEmpty(message)) return;
        String state = getState(message);
        ITerminalAuditHandler iTerminalAuditProcess = TerminalAuditHandlerFactory.getHandlerByKey(state);
        if (iTerminalAuditProcess != null) iTerminalAuditProcess.handle(message, session);
    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.debug("发生错误: err={}, SessionID={}", error.getMessage(), session.getId());
    }

}
