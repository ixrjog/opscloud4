package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.controller.base.SimpleAuthentication;
import com.baiyi.opscloud.controller.base.SimpleLogin;
import com.baiyi.opscloud.guacamole.tunnel.BaseGuacamoleTunnel;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
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

    private static SimpleAuthentication simpleAuthentication;

    @Autowired
    public void setSimpleAuthentication(SimpleAuthentication simpleAuthentication){
        GuacamoleController.simpleAuthentication = simpleAuthentication;
    }

    /**
     *
     * @param session
     * @param endpointConfig
     * @return
     * @throws GuacamoleException
     */
    @Override
    protected GuacamoleTunnel createTunnel(Session session, EndpointConfig endpointConfig) throws GuacamoleException {
        Map<String, List<String>> parameterMap = session.getRequestParameterMap();
        String token = getGuacamoleParam(parameterMap, "token");
        SimpleLogin simpleLogin = SimpleLogin.builder()
                .token(token)
                .build();
        String username = simpleAuthentication.authentication(simpleLogin);
        return super.createTunnel(session, endpointConfig);
    }

}
