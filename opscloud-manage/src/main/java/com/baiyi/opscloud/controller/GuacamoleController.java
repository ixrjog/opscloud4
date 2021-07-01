package com.baiyi.opscloud.controller;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.guacamole.BaseGuacamoleWebSocket;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleTunnel;
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
public final class GuacamoleController extends BaseGuacamoleWebSocket {

    /**
     * protocol = rdp/vnc
     * serverId = 1
     * accountType = 0/1
     *
     * @param session
     * @param endpointConfig
     * @return
     * @throws GuacamoleException
     */
    @Override
    protected GuacamoleTunnel createTunnel(Session session, EndpointConfig endpointConfig) throws GuacamoleException {
        Map<String, List<String>> parameterMap = session.getRequestParameterMap();
        System.err.println(JSON.toJSON(parameterMap));
        System.err.println("鉴权！！！！！");
        return super.createTunnel(session, endpointConfig);
    }

}
