package com.baiyi.opscloud.leo.task.loop;

import com.baiyi.opscloud.common.leo.session.LeoBuildQuerySessionMap;
import com.baiyi.opscloud.common.leo.session.LeoDeployQuerySessionMap;
import com.baiyi.opscloud.leo.message.factory.LeoContinuousDeliveryMessageHandlerFactory;
import com.baiyi.opscloud.leo.message.handler.base.ILeoContinuousDeliveryRequestHandler;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType.QUERY_LEO_BUILD_CONSOLE_STREAM;

/**
 * 用户构建事件循环
 *
 * @Author baiyi
 * @Date 2022/11/28 09:48
 * @Version 1.0
 */
@Slf4j
public class LeoBuildEventLoop implements Runnable {

    private final String sessionId;

    private final Session session;

    public LeoBuildEventLoop(String sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!this.session.isOpen()) {
                    log.warn("Leo build event loop end: sessionId={}", this.sessionId);
                    LeoDeployQuerySessionMap.removeSessionQueryMap(this.sessionId);
                    return;
                }
            } catch (Exception e) {
                try {
                    this.session.close();
                } catch (IOException ioException) {
                    log.warn("Leo build event loop session closed: sessionId={}", this.sessionId);
                    LeoDeployQuerySessionMap.removeSessionQueryMap(this.sessionId);
                    return;
                }
            }
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException ignored) {
            }
            try {
                if (LeoBuildQuerySessionMap.sessionQueryMapContainsKey(this.sessionId)) {
                    // 深copy
                    Map<String, String> queryMap = Maps.newHashMap(LeoBuildQuerySessionMap.getSessionQueryMap(this.sessionId));
                    queryMap.keySet().forEach(messageType -> {
                        if (messageType.equals(QUERY_LEO_BUILD_CONSOLE_STREAM.name())) {
                            // 不处理控制台日志流
                            return;
                        }
                        ILeoContinuousDeliveryRequestHandler handler = LeoContinuousDeliveryMessageHandlerFactory.getHandlerByMessageType(messageType);
                        if (handler != null) {
                            handler.handleRequest(this.sessionId, session, queryMap.get(messageType));
                        }
                    });
                }
            } catch (Exception e) {
                if (e instanceof NullPointerException) {
                    log.error("Leo build event loop: NullPointerException");
                    e.printStackTrace();
                } else {
                    log.error(e.getMessage());
                }
            }
        }
    }

}
