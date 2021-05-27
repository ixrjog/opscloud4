package com.baiyi.caesar.guacamole;

import com.baiyi.caesar.common.builder.GuacamoleConfigurationBuilder;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.apache.guacamole.websocket.GuacamoleWebSocketTunnelEndpoint;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/21 5:45 下午
 * @Version 1.0
 */
public class BaseGuacamoleWebSocket extends GuacamoleWebSocketTunnelEndpoint {

    private interface SupportProtocol {
        String KUBERNETES = "kubernetes";
        String RDP = "rdp";
        String SSH = "ssh";
        String TELNET = "telnet";
        String VNC = "vnc";
    }

    @Override
    protected GuacamoleTunnel createTunnel(Session session, EndpointConfig endpointConfig) throws GuacamoleException {

        GuacamoleConfiguration configuration = new GuacamoleConfiguration();
        configuration.setProtocol(SupportProtocol.RDP);
        Map<String, String> parameters = GuacamoleConfigurationBuilder.newBuilder()
                .paramEntry("hostname", "172.16.210.2")
                .paramEntry("username", "administrator")
                .paramEntry("port","3389")
                .paramEntry("security","any")
                .paramEntry("dpi","96")
                .paramEntry("password", "????")
                .paramEntry("username", "administrator")
                .paramEntry("ignore-cert", "true")
                .paramEntry("width", "1280")
                .paramEntry("height", "640")
                .build().getDict();
        configuration.setParameters(parameters);

        GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                new InetGuacamoleSocket("172.16.210.1", 4822),
                configuration
        );
        return new SimpleGuacamoleTunnel(socket);
    }

    @Override
    @OnMessage
    public void onMessage(String message) {
        // System.err.println(message);
        super.onMessage(message);
    }
}
