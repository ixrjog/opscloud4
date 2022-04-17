package com.baiyi.opscloud.controller.ws;

import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.controller.ws.base.SimpleAuthentication;
import com.baiyi.opscloud.terminal.audit.ITerminalAuditProcess;
import com.baiyi.opscloud.terminal.audit.TerminalAuditProcessFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author baiyi
 * @Date 2021/7/23 2:39 下午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/terminal/session/audit")
@Component
public class TerminalSessionAuditController extends SimpleAuthentication {

    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static final CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();

    private Session session = null;
    // 超时时间1H
    public static final Long WEBSOCKET_TIMEOUT = TimeUtil.hourTime;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        sessionSet.add(session);
        int cnt = onlineCount.incrementAndGet(); // 在线数加1
        log.info("终端会话审计有连接加入: 当前连接数为 = {}", cnt);
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
        this.session = session;
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        sessionSet.remove(session);
        int cnt = onlineCount.decrementAndGet();
        log.info("有连接关闭: 当前连接数为 = {}", cnt);
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
        ITerminalAuditProcess iTerminalAuditProcess = TerminalAuditProcessFactory.getProcessByKey(state);
        if (iTerminalAuditProcess != null) iTerminalAuditProcess.process(message, session);
    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误: e = {}，SessionID = {}", error.getMessage(), session.getId());
    }

}
