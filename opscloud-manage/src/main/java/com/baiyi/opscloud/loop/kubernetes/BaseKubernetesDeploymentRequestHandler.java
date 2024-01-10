package com.baiyi.opscloud.loop.kubernetes;

import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.UserToken;
import com.baiyi.opscloud.loop.kubernetes.factory.KubernetesDeploymentMessageHandlerFactory;
import com.baiyi.opscloud.service.user.UserTokenService;
import org.apache.commons.lang3.StringUtils;

import jakarta.annotation.Resource;
import jakarta.websocket.Session;

import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2023/3/27 19:45
 * @Version 1.0
 */
public abstract class BaseKubernetesDeploymentRequestHandler<T> implements IKubernetesDeploymentRequestHandler {

    @Resource
    private UserTokenService userTokenService;

    private static final String AUTHENTICATION_FAILURE = "AUTHENTICATION_FAILURE";

    protected String handleAuth(String token) throws AuthenticationException {
        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_REQUEST_NO_TOKEN);
        }
        UserToken userToken = userTokenService.getByValidToken(token);
        if (userToken == null) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_TOKEN_INVALID);
        }
        return userToken.getUsername();
    }

    protected void sendToSession(Session session, AuthenticationException e) throws IOException {
        if (session.isOpen()) {
            KubernetesDeploymentResponse<String> response = KubernetesDeploymentResponse.<String>builder()
                    .body(e.getMessage())
                    .messageType(AUTHENTICATION_FAILURE)
                    .build();
            session.getBasicRemote().sendText(response.toString());
        }
    }

    protected void sendToSession(Session session, T body) throws IOException {
        if (session.isOpen()) {
            KubernetesDeploymentResponse<T> response = KubernetesDeploymentResponse.<T>builder()
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
