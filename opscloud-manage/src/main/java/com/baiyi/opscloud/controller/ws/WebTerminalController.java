package com.baiyi.opscloud.controller.ws;

import com.baiyi.opscloud.common.model.HostInfo;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.controller.ws.base.SimpleAuthentication;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.builder.TerminalSessionBuilder;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.enums.SessionTypeEnum;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.sshcore.message.base.SimpleLoginMessage;
import com.baiyi.opscloud.sshcore.task.terminal.SentOutputTask;
import com.baiyi.opscloud.terminal.factory.TerminalProcessFactory;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author baiyi
 * @Date 2021/5/28 9:15 上午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/terminal")
@Component
public class WebTerminalController extends SimpleAuthentication {

    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static final ThreadLocal<CopyOnWriteArraySet<Session>> sessionSet = ThreadLocal.withInitial(CopyOnWriteArraySet::new);
    // 当前会话 uuid
    private final String sessionId = UUID.randomUUID().toString();

    private Session session = null;
    // 超时时间15分钟
    public static final Long WEBSOCKET_TIMEOUT = TimeUtil.minuteTime * 15;

    private static final HostInfo serverInfo = HostInfo.build();

    private static TerminalSessionService terminalSessionService;

    private TerminalSession terminalSession;

    @Autowired
    public void setTerminalSessionService(TerminalSessionService terminalSessionService) {
        WebTerminalController.terminalSessionService = terminalSessionService;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            log.info("WebTerm尝试连接，sessionId = {}", sessionId);
            TerminalSession terminalSession = TerminalSessionBuilder.build(sessionId, serverInfo, SessionTypeEnum.WEB_TERMINAL);
            terminalSessionService.add(terminalSession);
            this.terminalSession = terminalSession;
            sessionSet.get().add(session);
            int cnt = onlineCount.incrementAndGet(); // 在线数加1
            log.info("WebTerm有连接加入，当前连接数为：{}", cnt);
            session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
            this.session = session;
            // 线程启动
            Runnable run = new SentOutputTask(sessionId, session);
            Thread thread = new Thread(run);
            thread.start();
        } catch (Exception e) {
            log.error("WebTerm创建连接错误！");
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            TerminalProcessFactory.getProcessByKey(MessageState.CLOSE.getState()).process("", session, terminalSession);
            sessionSet.get().remove(session);
            int cnt = onlineCount.decrementAndGet();
            log.info("有连接关闭当前连接数为: {}", cnt);
        } catch (Exception e) {
            log.error("WebTerm OnClose错误！");
            e.printStackTrace();
        }
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
        if (StringUtils.isEmpty(this.terminalSession.getUsername())) {
            if (MessageState.LOGIN.getState().equals(state))       // 鉴权并更新会话信息
                updateSessionUsername(hasLogin(new GsonBuilder().create().fromJson(message, SimpleLoginMessage.class)));
        } else {
            SessionUtil.setUsername(this.terminalSession.getUsername());
        }
        TerminalProcessFactory.getProcessByKey(state).process(message, session, terminalSession);
    }

    private void updateSessionUsername(String username) {
        terminalSession.setUsername(username);
        terminalSessionService.update(terminalSession);
    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
        ServerMessage.BaseMessage closeMessage = ServerMessage.BaseMessage.builder()
                .state(MessageState.CLOSE.getState())
                .build();
        TerminalProcessFactory.getProcessByKey(MessageState.CLOSE.getState())
                .process(JSONUtil.writeValueAsString(closeMessage), session, terminalSession);
    }

}
