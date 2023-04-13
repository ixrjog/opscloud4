package com.baiyi.opscloud.sshcore.builder;

import com.baiyi.opscloud.common.model.HostInfo;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.enums.SessionTypeEnum;

import java.net.SocketAddress;

/**
 * @Author baiyi
 * @Date 2021/5/28 9:33 上午
 * @Version 1.0
 */
public class TerminalSessionBuilder {

    public static TerminalSession build(String sessionId, HostInfo serverInfo, SessionTypeEnum sessionTypeEnum) {
        return TerminalSession.builder()
                .sessionId(sessionId)
                .serverHostname(serverInfo.getHostname())
                .serverAddr(serverInfo.getHostAddress())
                .sessionClosed(false)
                .sessionType(sessionTypeEnum.getType())
                .build();
    }

    public static TerminalSession build(String sessionId, String username, HostInfo serverInfo, SessionTypeEnum sessionTypeEnum) {
        TerminalSession tSession = build(sessionId, serverInfo, sessionTypeEnum);
        tSession.setUsername(username);
        return tSession;
    }

    public static TerminalSession build(String sessionId, String username, HostInfo serverInfo, SocketAddress socketAddress, SessionTypeEnum sessionTypeEnum) {
        TerminalSession tSession = build(sessionId, username, serverInfo, sessionTypeEnum);
        tSession.setRemoteAddr(socketAddress.toString().replace("/", ""));
        return tSession;
    }

}