package com.baiyi.opscloud.ws;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baiyi.opscloud.bo.ServerAddr;
import com.baiyi.opscloud.builder.TerminalSessionBuilder;
import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.facade.AuthBaseFacade;
import com.baiyi.opscloud.facade.TerminalBaseFacade;
import com.baiyi.opscloud.factory.xterm.XTermProcessFactory;
import com.baiyi.opscloud.xterm.message.InitialMessage;
import com.baiyi.opscloud.xterm.task.SentOutputTask;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ServerEndpoint(value = "/ws/xterm")
@Component
public class XTermWSController implements InitializingBean {

    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();

    // 当前会话 uuid
    private final String sessionId = UUID.randomUUID().toString();

    private Session session = null;

    private static ServerAddr serverAddr = ServerAddr.builder().build();

    // 超时时间1H
    public static final Long WEBSOCKET_TIMEOUT = 60 * 60 * 1000L;

    private OcTerminalSession ocTerminalSession;

    private static TerminalBaseFacade terminalFacade;

    private static AuthBaseFacade authBaseFacade;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setTerminalFacade(TerminalBaseFacade terminalFacade) {
        XTermWSController.terminalFacade = terminalFacade;
    }

    @Autowired
    public void setAuthBaseFacade(AuthBaseFacade authBaseFacade) {
        XTermWSController.authBaseFacade = authBaseFacade;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
//        this.sessionId = UUID.randomUUID().toString();
        OcTerminalSession ocTerminalSession = TerminalSessionBuilder.build(sessionId, serverAddr);
        terminalFacade.addOcTerminalSession(ocTerminalSession);
        this.ocTerminalSession = ocTerminalSession;
        sessionSet.add(session);
        int cnt = onlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入，当前连接数为：{}", cnt);
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
        XTermProcessFactory.getIXTermProcessByKey(XTermRequestStatus.CLOSE.getCode()).xtermProcess("", session, ocTerminalSession);
        sessionSet.remove(session);
        int cnt = onlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
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
        // log.info("来自客户端的消息：{}", message);
        SessionUtils.setUsername(ocTerminalSession.getUsername());
        JSONObject jsonObject = JSON.parseObject(message);
        String status = jsonObject.getString("status");
        // 鉴权并更新会话信息
        if (XTermRequestStatus.INITIAL.getCode().equals(status) || XTermRequestStatus.INITIAL_IP.getCode().equals(status)) {
            InitialMessage xtermMessage = new GsonBuilder().create().fromJson(message, InitialMessage.class);
            String username = authBaseFacade.getUserByToken(xtermMessage.getToken());
            if (StringUtils.isEmpty(ocTerminalSession.getUsername())) {
                ocTerminalSession.setUsername(username);
                terminalFacade.updateOcTerminalSession(ocTerminalSession);
                SessionUtils.setUsername(ocTerminalSession.getUsername()); // 设置当前会话用户身份
            }
        }
        XTermProcessFactory.getIXTermProcessByKey(status).xtermProcess(message, session, ocTerminalSession);
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
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session
     * @param message
     */
    public static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     *
     * @param message
     * @throws IOException
     */
    public static void BroadCastInfo(String message) throws IOException {
        for (Session session : sessionSet) {
            if (session.isOpen()) {
                sendMessage(session, message);
            }
        }
    }

    /**
     * 指定Session发送消息
     *
     * @param sessionId
     * @param message
     * @throws IOException
     */
    public static void sendMessage(String message, String sessionId) throws IOException {
        Session session = null;
        for (Session s : sessionSet) {
            if (s.getId().equals(sessionId)) {
                session = s;
                break;
            }
        }
        if (session != null) {
            sendMessage(session, message);
        } else {
            log.warn("没有找到你指定ID的会话：{}", sessionId);
        }
    }

    @Override
    public void afterPropertiesSet() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            serverAddr = ServerAddr.builder()
                    .hostAddress(addr.getHostAddress())
                    .hostname(hostname)
                    .build();
        } catch (UnknownHostException e) {
        }
    }

}
