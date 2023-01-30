package com.baiyi.opscloud.controller.ws;

import com.baiyi.opscloud.common.model.HostInfo;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.common.util.ThreadPoolTaskExecutorPrint;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.controller.ws.base.SimpleAuthentication;
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

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
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

    // 当前会话 uuid
    private final String sessionId = UUID.randomUUID().toString();

    private Session session = null;
    // 超时时间15分钟
    public static final Long WEBSOCKET_TIMEOUT = TimeUtil.minuteTime * 5;

    private static final HostInfo serverInfo = HostInfo.build();

    private static TerminalSessionService terminalSessionService;

    private TerminalSession terminalSession;

    private static ThreadPoolTaskExecutor serverTerminalExecutor;

    private static final String IF_NAME = "Server terminal";

    @Autowired
    public void setTerminalSessionService(TerminalSessionService terminalSessionService) {
        ServerTerminalController.terminalSessionService = terminalSessionService;
    }

    @Resource
    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor serverTerminalExecutor) {
        ServerTerminalController.serverTerminalExecutor = serverTerminalExecutor;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            log.info("{} session try to connect: instanceIP={}, sessionId={}", IF_NAME, serverInfo.getHostAddress(), sessionId);
            TerminalSession terminalSession = TerminalSessionBuilder.build(sessionId, serverInfo, SessionTypeEnum.WEB_TERMINAL);
            terminalSessionService.add(terminalSession);
            this.terminalSession = terminalSession;
            session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
            this.session = session;
            // 线程启动
            serverTerminalExecutor.execute(new SentOutputTask(sessionId, session));
            ThreadPoolTaskExecutorPrint.print(serverTerminalExecutor, "serverTermExecutor");
        } catch (Exception e) {
            log.error("{} create connection error！", IF_NAME);
            log.error(e.getMessage());
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            ServerTerminalMessageHandlerFactory.getHandlerByState(MessageState.CLOSE.getState()).handle("", session, terminalSession);
        } catch (Exception e) {
            log.error("{} OnClose error！", IF_NAME);
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
        String state = getState(message);
        if (StringUtils.isEmpty(this.terminalSession.getUsername())) {
            if (MessageState.LOGIN.getState().equals(state))       // 鉴权并更新会话信息
                updateSessionUsername(hasLogin(new GsonBuilder().create().fromJson(message, SimpleLoginMessage.class)));
        } else {
            SessionUtil.setUsername(this.terminalSession.getUsername());
        }
        ServerTerminalMessageHandlerFactory
                .getHandlerByState(state)
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
        log.debug("{} error: instanceIP={}, err={}, sessionID={}",
                IF_NAME,
                serverInfo.getHostAddress(),
                error.getMessage(),
                session.getId());
    }

}
