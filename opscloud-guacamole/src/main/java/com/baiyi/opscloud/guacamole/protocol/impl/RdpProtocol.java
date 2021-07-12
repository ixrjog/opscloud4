package com.baiyi.opscloud.guacamole.protocol.impl;

import com.baiyi.opscloud.common.builder.GuacamoleConfigurationBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import com.baiyi.opscloud.domain.param.guacamole.GuacamoleParam;
import com.baiyi.opscloud.guacamole.tunnel.BaseGuacamoleTunnel;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/9 1:46 下午
 * @Version 1.0
 */
@Component
public class RdpProtocol extends AbstractGuacamoleProtocol {

    @Override
    public String getProtocol() {
        return BaseGuacamoleTunnel.SupportProtocol.RDP;
    }

    @Override
    protected Map<String, String> buildParameters(Server server, ServerAccount serverAccount, GuacamoleParam.Login guacamoleLogin) {
        Credential credential = getCredential(serverAccount);
        return GuacamoleConfigurationBuilder.newBuilder()
                .paramEntry("hostname", server.getPrivateIp())
                .paramEntry("username", serverAccount.getUsername())
                .paramEntry("password", credential.getCredential())
                .paramEntry("port", "3389")
                .paramEntry("security", "any")
                .paramEntry("ignore-cert", "true")
                .paramEntry("dpi", guacamoleLogin.getScreenDpi().toString())
                .paramEntry("width", guacamoleLogin.getScreenWidth().toString())
                .paramEntry("height", guacamoleLogin.getScreenHeight().toString())
                .build().getDict();
    }

}
