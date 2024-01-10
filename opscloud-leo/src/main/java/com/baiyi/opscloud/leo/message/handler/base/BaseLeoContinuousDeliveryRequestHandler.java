package com.baiyi.opscloud.leo.message.handler.base;

import com.baiyi.opscloud.common.leo.session.LeoDeployQuerySessionMap;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.leo.message.factory.LeoContinuousDeliveryMessageHandlerFactory;
import com.baiyi.opscloud.common.leo.response.LeoContinuousDeliveryResponse;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import jakarta.websocket.Session;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2022/11/23 16:11
 * @Version 1.0
 */
@SuppressWarnings("rawtypes")
@Slf4j
public abstract class BaseLeoContinuousDeliveryRequestHandler<T> implements ILeoContinuousDeliveryRequestHandler, InitializingBean {

    protected T toLeoRequestParam(String message, Class<T> targetClass) {
        return new GsonBuilder().create().fromJson(message, targetClass);
    }

    protected void sendToSession(Session session, LeoContinuousDeliveryResponse response) throws IOException {
        if (session.isOpen()) {
            session.getBasicRemote().sendText(response.toString());
        }
    }

    protected void sendToSession(Session session, DataTable body) throws IOException {
        if (session.isOpen()) {
            LeoContinuousDeliveryResponse<DataTable> response = LeoContinuousDeliveryResponse.<DataTable>builder()
                    .body(body)
                    .messageType(getMessageType())
                    .build();
            session.getBasicRemote().sendText(response.toString());
        }
    }

    /**
     * 取消订阅
     *
     * @param sessionId
     */
    protected void unsubscribe(String sessionId) {
        log.info("取消订阅: sessionId={}, messageType={}", sessionId, getMessageType());
        LeoDeployQuerySessionMap.removeSessionQueryMap(sessionId, getMessageType());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LeoContinuousDeliveryMessageHandlerFactory.register(this);
    }

}