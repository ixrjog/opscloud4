package com.baiyi.opscloud.loop.kubernetes.impl;

import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.domain.model.message.KubernetesDeploymentMessage;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.facade.application.ApplicationKubernetesFacade;
import com.baiyi.opscloud.loop.kubernetes.BaseKubernetesDeploymentRequestHandler;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.websocket.Session;

import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2023/3/20 17:20
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class KubernetesDeploymentQueryRequestHandler extends BaseKubernetesDeploymentRequestHandler<ApplicationVO.Kubernetes> {

    private final ApplicationKubernetesFacade kubernetesFacade;

    @Override
    public String getMessageType() {
        return "QUERY_KUBERNETES_DEPLOYMENT";
    }

    @Override
    public void handleRequest(String sessionId, Session session, String message) throws IOException {
        KubernetesDeploymentMessage kubernetesDeploymentMessage = new GsonBuilder().create().fromJson(message, KubernetesDeploymentMessage.class);
        try {
            String username = handleAuth(kubernetesDeploymentMessage.getToken());
            ApplicationVO.Kubernetes kubernetes = getBody(kubernetesDeploymentMessage, username);
            sendToSession(session, kubernetes);
        } catch (AuthenticationException e) {
            sendToSession(session, e);
        }
    }

    public ApplicationVO.Kubernetes getBody(KubernetesDeploymentMessage kubernetesDeploymentMessage, String username) {
        ApplicationParam.GetApplicationKubernetes getApplicationKubernetes = ApplicationParam.GetApplicationKubernetes.builder()
                .applicationId(kubernetesDeploymentMessage.getApplicationId())
                .envType(kubernetesDeploymentMessage.getEnvType())
                .extend(true)
                .build();
        return kubernetesFacade.getApplicationKubernetes(getApplicationKubernetes, username);
    }

}
