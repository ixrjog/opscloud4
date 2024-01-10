package com.baiyi.opscloud.facade.kubernetes.impl;

import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.model.message.KubernetesDeploymentMessage;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.facade.kubernetes.KubernetesTerminalFacade;
import com.baiyi.opscloud.loop.kubernetes.KubernetesDeploymentResponse;
import com.baiyi.opscloud.loop.kubernetes.impl.KubernetesDeploymentQueryRequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/5/22 17:02
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesTerminalFacadeImpl implements KubernetesTerminalFacade {

    private final KubernetesDeploymentQueryRequestHandler kubernetesDeploymentQueryRequestHandler;

    @Override
    public KubernetesDeploymentResponse<ApplicationVO.Kubernetes> getKubernetesDeployment(int applicationId, int envType) {
        final String messageType = kubernetesDeploymentQueryRequestHandler.getMessageType();
        KubernetesDeploymentMessage kubernetesDeploymentMessage = KubernetesDeploymentMessage.builder()
                .messageType(messageType)
                .applicationId(applicationId)
                .envType(envType)
                .build();
        ApplicationVO.Kubernetes body = kubernetesDeploymentQueryRequestHandler
                .getBody(kubernetesDeploymentMessage, SessionHolder.getUsername());
        return KubernetesDeploymentResponse.<ApplicationVO.Kubernetes>builder()
                .body(body)
                .messageType(messageType)
                .build();
    }

}