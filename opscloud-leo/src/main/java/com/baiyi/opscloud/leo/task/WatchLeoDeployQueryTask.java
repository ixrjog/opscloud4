package com.baiyi.opscloud.leo.task;

import com.baiyi.opscloud.common.leo.session.LeoDeployQuerySessionMap;
import com.baiyi.opscloud.leo.message.factory.LeoContinuousDeliveryMessageHandlerFactory;
import com.baiyi.opscloud.leo.message.handler.base.ILeoContinuousDeliveryRequestHandler;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2022/12/6 18:09
 * @Version 1.0
 */
@Slf4j
public class WatchLeoDeployQueryTask implements Runnable {

    private final String sessionId;

    private final Session session;

    public WatchLeoDeployQueryTask(String sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!this.session.isOpen()) {
                    log.info("WatchLeoDeployTask会话关闭任务退出！");
                    LeoDeployQuerySessionMap.removeSessionQueryMap(this.sessionId);
                    break;
                }
                if (LeoDeployQuerySessionMap.sessionQueryMapContainsKey(this.sessionId)) {
                    Map<String, String> queryMap = LeoDeployQuerySessionMap.getSessionQueryMap(this.sessionId);
                    queryMap.keySet().forEach(messageType -> {
                        ILeoContinuousDeliveryRequestHandler handler = LeoContinuousDeliveryMessageHandlerFactory.getHandlerByMessageType(messageType);
                        if (handler != null) {
                            handler.handleRequest(this.sessionId, session, queryMap.get(messageType));
                        }
                    });
                }
                TimeUnit.SECONDS.sleep(5L);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

}
