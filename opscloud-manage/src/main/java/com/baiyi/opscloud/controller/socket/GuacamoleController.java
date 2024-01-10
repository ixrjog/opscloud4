package com.baiyi.opscloud.controller.socket;

import com.baiyi.opscloud.controller.socket.base.SimpleAuthentication;
import com.baiyi.opscloud.guacamole.tunnel.BaseGuacamoleTunnel;
import com.baiyi.opscloud.sshcore.message.base.SimpleLoginMessage;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/21 3:58 下午
 * @Version 1.0
 */
@ServerEndpoint(value = "/api/ws/guacamole/tunnel", subprotocols = "guacamole")
@Component
public final class GuacamoleController extends BaseGuacamoleTunnel {

    // extends BaseGuacamoleTunnel
    private static SimpleAuthentication simpleAuthentication;

    @Autowired
    public void setSimpleAuthentication(SimpleAuthentication simpleAuthentication) {
        GuacamoleController.simpleAuthentication = simpleAuthentication;
    }

    /**
     * @param session
     * @param endpointConfig
     * @return
     * @throws GuacamoleException
     */
    @Override
    protected GuacamoleTunnel createTunnel(Session session, EndpointConfig endpointConfig) throws GuacamoleException {
        Map<String, List<String>> parameterMap = session.getRequestParameterMap();
        String token = getGuacamoleParam(parameterMap, "token");
        SimpleLoginMessage simpleLogin = SimpleLoginMessage.builder()
                .token(token)
                .build();
        String username = simpleAuthentication.hasLogin(simpleLogin);
        return super.createTunnel(session, endpointConfig);
    }

}