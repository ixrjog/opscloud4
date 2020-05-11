package com.baiyi.opscloud.ws;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.factory.xterm.XTermProcessFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ServerEndpoint(value = "/ws/xterm")
@Component
public class XtermWSController {

    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();

   // private static final String sessionId = UUID.randomUUID().toString();


//    private static OcAuthFacade ocAuthFacade;
//
//    private static OcUserService ocUserService;
//
//    private static ServerGroupFacade serverGroupFacade;
//
//    private static KeyboxFacade keyboxFacade;
//
//    @Autowired
//    public void setOcAuthFacade(OcAuthFacade ocAuthFacade) {
//        XtermWSController.ocAuthFacade = ocAuthFacade;
//    }
//
//    @Autowired
//    public void setOcUserService(OcUserService ocUserService) {
//        XtermWSController.ocUserService = ocUserService;
//    }
//
//    @Autowired
//    public void setServerGroupFacade(ServerGroupFacade serverGroupFacade) {
//        XtermWSController.serverGroupFacade = serverGroupFacade;
//    }
//
//    @Autowired
//    public void setKeyboxFacade(KeyboxFacade keyboxFacade) {
//        XtermWSController.keyboxFacade = keyboxFacade;
//    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        sessionSet.add(session);
        int cnt = onlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入，当前连接数为：{}", cnt);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        XTermProcessFactory.getIXTermProcessByKey(XTermRequestStatus.CLOSE.getCode()).xtermProcess("",session);
        sessionSet.remove(session);
        int cnt = onlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息：{}", message);
        JSONObject jsonObject = JSON.parseObject(message);
        String status = jsonObject.getString("status");
        XTermProcessFactory.getIXTermProcessByKey(status).xtermProcess(message,session);
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
//            session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)",message,session.getId()));
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

}
