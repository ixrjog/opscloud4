package com.baiyi.opscloud.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author baiyi
 * @Date 2020/3/28 12:21 上午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/ws/create/cloud/instance/task")
@Component
public class CloudInstanceWSController {

        @PostConstruct
        public void init() {
            log.info("创建云实例websocket加载");
        }

        private static final AtomicInteger OnlineCount = new AtomicInteger(0);
        // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
        private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();

        /**
         * 连接建立成功调用的方法
         */
        @OnOpen
        public void onOpen(Session session) {
            SessionSet.add(session);
            int cnt = OnlineCount.incrementAndGet(); // 在线数加1
            log.info("有连接加入，当前连接数为：{}", cnt);
            SendMessage(session, "连接成功");
        }

        /**
         * 连接关闭调用的方法
         */
        @OnClose
        public void onClose(Session session) {
            SessionSet.remove(session);
            int cnt = OnlineCount.decrementAndGet();
            log.info("有连接关闭，当前连接数为：{}", cnt);
        }

        /**
         * 收到客户端消息后调用的方法
         *
         * @param message
         *            客户端发送过来的消息
         */
        @OnMessage
        public void onMessage(String message, Session session) {
            log.info("来自客户端的消息：{}",message);
            SendMessage(session, "收到消息，消息内容："+message);

        }

        /**
         * 出现错误
         * @param session
         * @param error
         */
        @OnError
        public void onError(Session session, Throwable error) {
            log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
            error.printStackTrace();
        }

        /**
         * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
         * @param session
         * @param message
         */
        public static void SendMessage(Session session, String message) {
            try {
                session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)",message,session.getId()));
            } catch (IOException e) {
                log.error("发送消息出错：{}", e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * 群发消息
         * @param message
         * @throws IOException
         */
        public static void BroadCastInfo(String message) throws IOException {
            for (Session session : SessionSet) {
                if(session.isOpen()){
                    SendMessage(session, message);
                }
            }
        }

        /**
         * 指定Session发送消息
         * @param sessionId
         * @param message
         * @throws IOException
         */
        public static void SendMessage(String message,String sessionId) throws IOException {
            Session session = null;
            for (Session s : SessionSet) {
                if(s.getId().equals(sessionId)){
                    session = s;
                    break;
                }
            }
            if(session!=null){
                SendMessage(session, message);
            }
            else{
                log.warn("没有找到你指定ID的会话：{}",sessionId);
            }
        }





}
