package com.baiyi.opscloud.guacamole.protocol.impl;

import com.baiyi.opscloud.common.builder.GuacamoleConfigurationBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.param.guacamole.GuacamoleParam;
import com.baiyi.opscloud.guacamole.tunnel.BaseGuacamoleTunnel;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/9 5:16 下午
 * @Version 1.0
 */
@Component
public class VncProtocol extends AbstractGuacamoleProtocol {

    @Override
    public String getProtocol() {
        return BaseGuacamoleTunnel.SupportProtocol.VNC;
    }

    @Override
    protected Map<String, String> buildParameters(Server server, ServerAccount serverAccount, GuacamoleParam.Login guacamoleLogin) {
        Credential credential = getCredential(serverAccount);
        ServerProperty.Server serverProperty = getBusinessProperty(server);
        return GuacamoleConfigurationBuilder.newBuilder()
                .putParam("hostname", server.getPrivateIp())
                .putParam("username", serverAccount.getUsername())
                .putParam("password", credential.getCredential())
                .putParam("port", String.valueOf(serverProperty.getMetadata().getVncPort()))
                .putParam("ignore-cert", "true")
                .putParam("dpi", guacamoleLogin.getScreenDpi().toString())
                .putParam("width", guacamoleLogin.getScreenWidth().toString())
                .putParam("height", guacamoleLogin.getScreenHeight().toString())
                .build()
                .getDict();
    }

}