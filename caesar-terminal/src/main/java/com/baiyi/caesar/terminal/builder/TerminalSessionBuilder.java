package com.baiyi.caesar.terminal.builder;

import com.baiyi.caesar.common.model.HostInfo;
import com.baiyi.caesar.domain.generator.caesar.TerminalSession;

/**
 * @Author baiyi
 * @Date 2021/5/28 9:33 上午
 * @Version 1.0
 */
public class TerminalSessionBuilder {

    public static TerminalSession build(String sessionId, HostInfo serverInfo) {
        return TerminalSession.builder()
                .sessionId(sessionId)
                .serverHostname(serverInfo.getHostname())
                .serverAddr(serverInfo.getHostAddress())
                .sessionClosed(false)
                .build();
    }

}
