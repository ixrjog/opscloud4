package com.baiyi.opscloud.controller.ws;

import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.controller.ws.base.SimpleAuthentication;
import com.baiyi.opscloud.datasource.ansible.play.ITaskPlayProcessor;
import com.baiyi.opscloud.datasource.ansible.play.ServerTaskPlayFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author baiyi
 * @Date 2021/9/26 3:33 下午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/server/task/play")
@Component
public class ServerTaskPlayController extends SimpleAuthentication {

    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    // concurrent包是线程安全Set，用来存放每个客户端对应的Session对象。
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
        log.info("剧本任务日志有连接加入: 当前连接数为：{}", cnt);
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
        this.session = session;
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        sessionSet.remove(session);
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
        ITaskPlayProcessor iTaskPlayProcess = ServerTaskPlayFactory.getProcessByKey(state);
        if (iTaskPlayProcess != null) iTaskPlayProcess.process(message, session);
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
    }

}

