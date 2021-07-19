package com.baiyi.opscloud.sshcore.builder;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.sshcore.model.HostSystem;
import com.baiyi.opscloud.sshcore.model.KubernetesResource;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/5/28 11:31 上午
 * @Version 1.0
 */
public class TerminalSessionInstanceBuilder {

    private static final String DEFAULT_LOGIN_USER = "SYSTEM";

    public static TerminalSessionInstance build(TerminalSession terminalSession, HostSystem hostSystem) {
        return TerminalSessionInstance.builder()
                .sessionId(terminalSession.getSessionId())
                .instanceId(hostSystem.getInstanceId())
                .loginUser(hostSystem.getSshCredential() != null ? hostSystem.getSshCredential().getServerAccount().getUsername() : null)
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

    public static TerminalSessionInstance build(TerminalSession terminalSession, KubernetesResource.Pod pod, String instanceId) {
        return TerminalSessionInstance.builder()
                .sessionId(terminalSession.getSessionId())
                .instanceId(instanceId)
                .loginUser(DEFAULT_LOGIN_USER)
                .hostIp(pod.getPodIp())
                .outputSize(0L)
                .openTime(new Date())
                .instanceClosed(false)
                .build();
    }
}
