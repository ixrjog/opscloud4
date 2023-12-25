package com.baiyi.opscloud.leo.task.loop;

import com.baiyi.opscloud.common.leo.session.LeoDeployQuerySessionMap;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.leo.message.factory.LeoContinuousDeliveryMessageHandlerFactory;
import com.baiyi.opscloud.leo.message.handler.base.ILeoContinuousDeliveryRequestHandler;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import jakarta.websocket.Session;
import java.util.Map;

/**
 * 用户部署事件循环
 *
 * @Author baiyi
 * @Date 2022/12/6 18:09
 * @Version 1.0
 */
@Slf4j
public class LeoDeployEventLoop implements Runnable {

    private final String sessionId;

    private final Session session;

    public LeoDeployEventLoop(String sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;
    }

    @Override
    public void run() {
        while (true) {
            if (!this.session.isOpen()) {
                log.debug("Leo deploy event loop end: sessionId={}", this.sessionId);
                LeoDeployQuerySessionMap.removeSessionQueryMap(this.sessionId);
                return;
            }
            try {
                if (LeoDeployQuerySessionMap.sessionQueryMapContainsKey(this.sessionId)) {
                    // 深copy
                    Map<String, String> queryMap = Maps.newHashMap(LeoDeployQuerySessionMap.getSessionQueryMap(this.sessionId));
                    if (!queryMap.isEmpty()) {
                        for (String messageType : queryMap.keySet()) {
                            ILeoContinuousDeliveryRequestHandler handler = LeoContinuousDeliveryMessageHandlerFactory.getHandlerByMessageType(messageType);
                            if (handler != null) {
                                handler.handleRequest(this.sessionId, session, queryMap.get(messageType));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Leo deploy event loop error: {}", e.getMessage());
            }
            NewTimeUtil.sleep(3L);
        }
    }

}