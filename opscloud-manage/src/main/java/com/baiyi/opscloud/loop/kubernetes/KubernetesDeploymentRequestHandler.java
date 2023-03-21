package com.baiyi.opscloud.loop.kubernetes;

import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.UserToken;
import com.baiyi.opscloud.domain.model.message.KubernetesDeploymentMessage;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.facade.application.ApplicationFacade;
import com.baiyi.opscloud.service.user.UserTokenService;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2023/3/20 17:20
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class KubernetesDeploymentRequestHandler implements IKubernetesDeploymentRequestHandler {

    private final ApplicationFacade applicationFacade;

    private final UserTokenService userTokenService;

    @Override
    public String getMessageType() {
        return "QUERY_KUBERNETES_DEPLOYMENT";
    }

    @Override
    public void handleRequest(String sessionId, Session session, String message) throws IOException {
        KubernetesDeploymentMessage kubernetesDeploymentMessage = new GsonBuilder().create().fromJson(message, KubernetesDeploymentMessage.class);
        ApplicationParam.GetApplicationKubernetes getApplicationKubernetes = ApplicationParam.GetApplicationKubernetes.builder()
                .applicationId(kubernetesDeploymentMessage.getApplicationId())
                .envType(kubernetesDeploymentMessage.getEnvType())
                .extend(true)
                .build();

        if (StringUtils.isEmpty(SessionUtil.getUsername())) {
            if (StringUtils.isBlank(kubernetesDeploymentMessage.getToken())) {
                return;
            }
            UserToken userToken = userTokenService.getByVaildToken(kubernetesDeploymentMessage.getToken());
            if (userToken == null) {
                return;
            }
            // 设置当前会话用户身份
            SessionUtil.setUsername(userToken.getUsername());
        }
        ApplicationVO.Application application = applicationFacade.getApplicationKubernetes(getApplicationKubernetes);
        sendToSession(session, application);
    }

    protected void sendToSession(Session session, ApplicationVO.Application body) throws IOException {
        if (session.isOpen()) {
            KubernetesDeploymentResponse response = KubernetesDeploymentResponse.builder()
                    .body(body)
                    .messageType(getMessageType())
                    .build();
            session.getBasicRemote().sendText(response.toString());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        KubernetesDeploymentMessageHandlerFactory.register(this);
    }

}
