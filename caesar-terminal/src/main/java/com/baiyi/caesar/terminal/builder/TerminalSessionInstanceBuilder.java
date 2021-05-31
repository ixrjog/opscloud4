package com.baiyi.caesar.terminal.builder;

import com.baiyi.caesar.domain.generator.caesar.TerminalSession;
import com.baiyi.caesar.domain.generator.caesar.TerminalSessionInstance;
import com.baiyi.caesar.terminal.model.HostSystem;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/5/28 11:31 上午
 * @Version 1.0
 */
public class TerminalSessionInstanceBuilder {

    public static TerminalSessionInstance build(TerminalSession terminalSession, HostSystem hostSystem) {
        return TerminalSessionInstance.builder()
                .sessionId(terminalSession.getSessionId())
                .instanceId(hostSystem.getInstanceId())
                .loginUser(hostSystem.getSshCredential().getServerAccount().getUsername())
                .hostIp(hostSystem.getHost())
                .outputSize(0L)
                .openTime(new Date())
                .instanceClosed(false)
                .build();
    }
    public static TerminalSessionInstance build(TerminalSession terminalSession, HostSystem hostSystem, String duplicateInstanceId) {
        TerminalSessionInstance terminalSessionInstance = build(terminalSession, hostSystem);
        terminalSessionInstance.setDuplicateInstanceId(duplicateInstanceId);
        return terminalSessionInstance;
    }
}
