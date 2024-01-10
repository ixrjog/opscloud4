package com.baiyi.opscloud.guacamole.tunnel;

import com.baiyi.opscloud.domain.param.guacamole.GuacamoleParam;
import com.baiyi.opscloud.guacamole.protocol.GuacamoleProtocolFactory;
import com.baiyi.opscloud.guacamole.protocol.IGuacamoleProtocol;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.websocket.GuacamoleWebSocketTunnelEndpoint;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/21 5:45 下午
 * @Version 1.0
 */
public class BaseGuacamoleTunnel  extends GuacamoleWebSocketTunnelEndpoint {

    public interface SupportProtocol {
        String KUBERNETES = "kubernetes";
        String RDP = "rdp";
        String SSH = "ssh";
        String TELNET = "telnet";
        String VNC = "vnc";
    }

    private GuacamoleParam.Login toGuacamoleLogin(Map<String, List<String>> parameterMap) {
        return GuacamoleParam.Login.builder()
                .serverId(Integer.parseInt(getGuacamoleParam(parameterMap, "serverId")))
                .serverAccountId(Integer.parseInt(getGuacamoleParam(parameterMap, "serverAccountId")))
                .protocol(getGuacamoleParam(parameterMap, "protocol"))
                .screenHeight(Integer.parseInt(getGuacamoleParam(parameterMap, "screenHeight")))
                .screenWidth(Integer.parseInt(getGuacamoleParam(parameterMap, "screenWidth")))
                .screenDpi(Integer.parseInt(getGuacamoleParam(parameterMap, "screenDpi")))
                .build();
    }

    @Override
    protected GuacamoleTunnel createTunnel(Session session, EndpointConfig endpointConfig) throws GuacamoleException {
        GuacamoleParam.Login guacamoleLogin = toGuacamoleLogin(session.getRequestParameterMap());
        IGuacamoleProtocol iGuacamoleProtocol = GuacamoleProtocolFactory.getProtocol(guacamoleLogin.getProtocol());
        GuacamoleSocket socket = iGuacamoleProtocol.buildGuacamoleSocket(guacamoleLogin);
        return new SimpleGuacamoleTunnel(socket);
    }

    @Override
    @OnMessage(maxMessageSize = 1024 * 1024)
    public void onMessage(String message) {
        super.onMessage(message);
    }

    protected String getGuacamoleParam(Map<String, List<String>> parameterMap, String key) {
        List<String> params = parameterMap.get(key);
        return params.getFirst();
    }

}