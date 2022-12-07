package com.baiyi.opscloud.leo.task;

import com.baiyi.opscloud.common.leo.session.LeoBuildQuerySessionMap;
import com.baiyi.opscloud.leo.message.factory.LeoContinuousDeliveryMessageHandlerFactory;
import com.baiyi.opscloud.leo.message.handler.base.ILeoContinuousDeliveryRequestHandler;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType.QUERY_LEO_BUILD_CONSOLE_STREAM;

/**
 * @Author baiyi
 * @Date 2022/11/28 09:48
 * @Version 1.0
 */
@Slf4j
public class WatchLeoBuildQueryTask implements Runnable {

    private final String sessionId;

    private final Session session;

    public WatchLeoBuildQueryTask(String sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!this.session.isOpen()) {
                    log.info("WatchLeoQueryTask会话关闭任务退出！");
                    LeoBuildQuerySessionMap.removeSessionQueryMap(this.sessionId);
                    break;
                }
                if (LeoBuildQuerySessionMap.sessionQueryMapContainsKey(this.sessionId)) {
                    Map<String, String> queryMap = LeoBuildQuerySessionMap.getSessionQueryMap(this.sessionId);
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
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException ie) {
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

}
