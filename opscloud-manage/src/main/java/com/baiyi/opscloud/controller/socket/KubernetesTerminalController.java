package com.baiyi.opscloud.controller.socket;

import com.baiyi.opscloud.common.model.HostInfo;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.controller.socket.base.SimpleAuthentication;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.factory.KubernetesTerminalMessageHandlerFactory;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.builder.TerminalSessionBuilder;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.enums.SessionTypeEnum;
import com.baiyi.opscloud.sshcore.message.base.SimpleLoginMessage;
import com.baiyi.opscloud.sshcore.task.terminal.SentOutputTask;
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
 * @Date 2021/7/14 5:06 下午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/kubernetes/terminal")
@Component
public class KubernetesTerminalController extends SimpleAuthentication {

    /**
     * 当前会话 UUID
     */
    private final String sessionId = UUID.randomUUID().toString();

    /**
     * 超时时间1H
     */
    public static final Long WEBSOCKET_TIMEOUT = NewTimeUtil.MINUTE_TIME * 15;

    private static final HostInfo SERVER_INFO = HostInfo.build();

    private TerminalSession terminalSession;

    private static TerminalSessionService terminalSessionService;

    private static ThreadPoolTaskExecutor xTerminalExecutor;

    @Autowired
    public void setTerminalSessionService(TerminalSessionService terminalSessionService) {
        KubernetesTerminalController.terminalSessionService = terminalSessionService;
    }

    @Resource
    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor xTerminalExecutor) {
        KubernetesTerminalController.xTerminalExecutor = xTerminalExecutor;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            log.info("Kubernetes terminal session try to connect: instanceIP={}, sessionId={}", SERVER_INFO.getHostAddress(), sessionId);
            TerminalSession terminalSession = TerminalSessionBuilder.build(sessionId, SERVER_INFO, SessionTypeEnum.KUBERNETES_TERMINAL);
            this.terminalSession = terminalSession;
            terminalSessionService.add(terminalSession);
            session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
            xTerminalExecutor.execute(new SentOutputTask(sessionId, session));
        } catch (Exception e) {
            log.error("Kubernetes terminal create connection error: {}", e.getMessage());
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        KubernetesTerminalMessageHandlerFactory.getHandlerByState(MessageState.CLOSE.getState())
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
            // 鉴权并更新会话信息
            if (MessageState.LOGIN.getState().equals(state)) {
                updateSessionUsername(hasLogin(new GsonBuilder().create().fromJson(message, SimpleLoginMessage.class)));
            }
        } else {
            SessionHolder.setUsername(this.terminalSession.getUsername());
        }
        KubernetesTerminalMessageHandlerFactory.getHandlerByState(state).handle(message, session, terminalSession);
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