package com.baiyi.opscloud.loop;

import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.ws.KubernetesDeploymentQuerySessionMap;
import com.baiyi.opscloud.domain.model.message.KubernetesDeploymentMessage;
import com.baiyi.opscloud.loop.kubernetes.IKubernetesDeploymentRequestHandler;
import com.baiyi.opscloud.loop.kubernetes.factory.KubernetesDeploymentMessageHandlerFactory;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import jakarta.websocket.Session;

/**
 * @Author baiyi
 * @Date 2023/3/20 16:35
 * @Version 1.0
 */
@Slf4j
public class KubernetesDeploymentEventLoop implements Runnable {

    private final String sessionId;

    private final Session session;

    public KubernetesDeploymentEventLoop(String sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;
    }

    @Override
    public void run() {
        while (true) {
            if (!this.session.isOpen()) {
                KubernetesDeploymentQuerySessionMap.removeSessionQueryMap(this.sessionId);
                return;
            }
            try {
                if (KubernetesDeploymentQuerySessionMap.sessionQueryMapContainsKey(this.sessionId)) {
                    String message = KubernetesDeploymentQuerySessionMap.getSessionQueryMap(this.sessionId);
                    KubernetesDeploymentMessage kubernetesDeploymentMessage = new GsonBuilder().create().fromJson(message, KubernetesDeploymentMessage.class);
                    IKubernetesDeploymentRequestHandler handler = KubernetesDeploymentMessageHandlerFactory.getHandlerByMessageType(kubernetesDeploymentMessage.getMessageType());
                    if (handler != null) {
                        handler.handleRequest(this.sessionId, session, message);
                        NewTimeUtil.sleep(10L);
                    }
                }
            } catch (Exception e) {
                if (e instanceof AuthenticationException) {
                    log.warn(e.getMessage());
                    return;
                }
                log.warn("Query kubernetes deployment error: {}", e.getMessage());
            }
            NewTimeUtil.sleep(2L);
        }
    }

}
