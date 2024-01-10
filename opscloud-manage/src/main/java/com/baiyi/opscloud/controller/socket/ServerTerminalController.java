package com.baiyi.opscloud.controller.socket;

import com.baiyi.opscloud.common.model.HostInfo;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.controller.socket.base.SimpleAuthentication;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.builder.TerminalSessionBuilder;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.enums.SessionTypeEnum;
import com.baiyi.opscloud.sshcore.message.base.SimpleLoginMessage;
import com.baiyi.opscloud.sshcore.task.terminal.SentOutputTask;
import com.baiyi.opscloud.terminal.factory.ServerTerminalMessageHandlerFactory;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2021/5/28 9:15 上午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/terminal")
@Component
public class ServerTerminalController extends SimpleAuthentication {

    /**
     * 当前会话UUID
     */
    private final String sessionId = UUID.randomUUID().toString();

    /**
     * 超时时间5分钟
     */
    public static final Long WEBSOCKET_TIMEOUT = NewTimeUtil.MINUTE_TIME * 5;

    private static final HostInfo SERVER_INFO = HostInfo.build();

    private static TerminalSessionService terminalSessionService;

    private TerminalSession terminalSession;

    private static ThreadPoolTaskExecutor xTerminalExecutor;

    @Autowired
    public void setTerminalSessionService(TerminalSessionService terminalSessionService) {
        ServerTerminalController.terminalSessionService = terminalSessionService;
    }

    @Resource
    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor xTerminalExecutor) {
        ServerTerminalController.xTerminalExecutor = xTerminalExecutor;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            log.info("Server terminal session try to connect: instanceIP={}, sessionId={}", SERVER_INFO.getHostAddress(), sessionId);
            TerminalSession terminalSession = TerminalSessionBuilder.build(sessionId, SERVER_INFO, SessionTypeEnum.WEB_TERMINAL);
            terminalSessionService.add(terminalSession);
            this.terminalSession = terminalSession;
            session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
            // 线程启动
            xTerminalExecutor.execute(new SentOutputTask(sessionId, session));
        } catch (Exception e) {
            log.error("Server terminal create connection error: {}", e.getMessage());
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        ServerTerminalMessageHandlerFactory.getHandlerByState(MessageState.CLOSE.getState())
                .handle("", session, terminalSession);
    }

    /**
     * 收到客户端消息后调用的方法
     * Session session
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage(maxMessageSize = 20 * 1024)
    public void onMessage(String message, Session session) {
        if (!session.isOpen() || StringUtils.isEmpty(message)) {
            return;
        }
        String state = getState(message);
        if (StringUtils.isEmpty(this.terminalSession.getUsername())) {
            if (MessageState.LOGIN.getState().equals(state)) {
                // 鉴权并更新会话信息
                updateSessionUsername(hasLogin(new GsonBuilder().create().fromJson(message, SimpleLoginMessage.class)));
            }
        } else {
            SessionHolder.setUsername(this.terminalSession.getUsername());
        }
        ServerTerminalMessageHandlerFactory.getHandlerByState(state)
                .handle(message, session, terminalSession);
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
    }

}