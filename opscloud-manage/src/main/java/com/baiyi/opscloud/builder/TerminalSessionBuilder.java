package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.bo.ServerAddr;
import com.baiyi.opscloud.bo.TerminalSessionBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;

/**
 * @Author baiyi
 * @Date 2020/5/24 12:46 下午
 * @Version 1.0
 */
public class TerminalSessionBuilder {

    public static OcTerminalSession build(String sessionId, ServerAddr serverAddr) {
        TerminalSessionBO terminalSessionBO = TerminalSessionBO.builder()
                .sessionId(sessionId)
                .termHostname(serverAddr.getHostname())
                .termAddr(serverAddr.getHostAddress())
                .build();
        return covert(terminalSessionBO);
    }

    private static OcTerminalSession covert(TerminalSessionBO terminalSessionBO) {
        return BeanCopierUtils.copyProperties(terminalSessionBO, OcTerminalSession.class);
    }
}
