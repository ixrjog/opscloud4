package com.baiyi.opscloud.sshcore.builder;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.sshcore.enums.InstanceSessionTypeEnum;
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

    public static TerminalSessionInstance build(String sessionId, HostSystem hostSystem, InstanceSessionTypeEnum instanceSessionTypeEnum) {
        return TerminalSessionInstance.builder()
                .sessionId(sessionId)
                .instanceId(hostSystem.getInstanceId())
                .loginUser(hostSystem.getSshCredential() != null ? hostSystem.getSshCredential().getServerAccount().getUsername() : null)
                .hostIp(hostSystem.getHost())
                .outputSize(0L)
                .openTime(new Date())
                .instanceSessionType(instanceSessionTypeEnum.getType())
                .instanceClosed(false)
                .build();
    }

    public static TerminalSessionInstance build(TerminalSession terminalSession, HostSystem hostSystem, InstanceSessionTypeEnum instanceSessionTypeEnum) {
        return build(terminalSession.getSessionId(), hostSystem, instanceSessionTypeEnum);
    }

    public static TerminalSessionInstance build(TerminalSession terminalSession, HostSystem hostSystem, String duplicateInstanceId, InstanceSessionTypeEnum instanceSessionTypeEnum) {
        TerminalSessionInstance terminalSessionInstance = build(terminalSession, hostSystem, instanceSessionTypeEnum);
        terminalSessionInstance.setDuplicateInstanceId(duplicateInstanceId);
        return terminalSessionInstance;
    }

    public static TerminalSessionInstance build(String sessionId, KubernetesResource.Pod pod, String instanceId, InstanceSessionTypeEnum instanceSessionTypeEnum) {
        return build(sessionId, pod.getPodIp(), instanceId, instanceSessionTypeEnum);
    }

    public static TerminalSessionInstance build(String sessionId, String podIp, String instanceId, InstanceSessionTypeEnum instanceSessionTypeEnum) {
        return TerminalSessionInstance.builder()
                .sessionId(sessionId)
                .instanceId(instanceId)
                .loginUser(DEFAULT_LOGIN_USER)
                .hostIp(podIp)
                .outputSize(0L)
                .openTime(new Date())
                .instanceSessionType(instanceSessionTypeEnum.getType())
                .instanceClosed(false)
                .build();
    }

}