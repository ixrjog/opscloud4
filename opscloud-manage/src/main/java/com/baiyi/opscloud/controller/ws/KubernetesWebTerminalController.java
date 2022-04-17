package com.baiyi.opscloud.controller.ws;

import com.baiyi.opscloud.common.model.HostInfo;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.controller.ws.base.SimpleAuthentication;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.factory.KubernetesTerminalProcessFactory;
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
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author baiyi
 * @Date 2021/7/14 5:06 下午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/kubernetes/terminal")
@Component
public class KubernetesWebTerminalController extends SimpleAuthentication {

    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static final CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();
    // 当前会话 uuid
    private final String sessionId = UUID.randomUUID().toString();

    private Session session = null;
    // 超时时间1H
    public static final Long WEBSOCKET_TIMEOUT = TimeUtil.hourTime;

    private static final HostInfo serverInfo = HostInfo.build();

    private TerminalSession terminalSession;

    private static TerminalSessionService terminalSessionService;

    @Autowired
    public void setTerminalSessionService(TerminalSessionService terminalSessionService) {
        KubernetesWebTerminalController.terminalSessionService = terminalSessionService;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("Kubernetes终端会话尝试链接: instanceIP = {} , sessionId = {}", serverInfo.getHostAddress(), sessionId);
        TerminalSession terminalSession = TerminalSessionBuilder.build(sessionId, serverInfo, SessionTypeEnum.KUBERNETES_TERMINAL);
        this.terminalSession = terminalSession;
        terminalSessionService.add(terminalSession);
        sessionSet.add(session);
        int cnt = onlineCount.incrementAndGet(); // 在线数加1
        log.info("Kubernetes终端会话有连接加入: instanceIP = {} , 当前连接数为 = {}", serverInfo.getHostAddress(), cnt);
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
        this.session = session;
        // 线程启动
        Runnable run = new SentOutputTask(sessionId, session);
        Thread thread = new Thread(run);
        thread.start();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        KubernetesTerminalProcessFactory.getProcessByKey(MessageState.CLOSE.getState()).process("", session, terminalSession);
        sessionSet.remove(session);
        int cnt = onlineCount.decrementAndGet();
        log.info("Kubernetes终端会话有连接关闭: instanceIP = {} , 当前连接数为 = {}", serverInfo.getHostAddress(), cnt);
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
        KubernetesTerminalProcessFactory.getProcessByKey(state).process(message, session, terminalSession);
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
        log.error("Kubernetes终端会话发生错误: instanceIP = {} , e = {}，sessionID = {}", serverInfo.getHostAddress(), error.getMessage(), session.getId());
    }

}
