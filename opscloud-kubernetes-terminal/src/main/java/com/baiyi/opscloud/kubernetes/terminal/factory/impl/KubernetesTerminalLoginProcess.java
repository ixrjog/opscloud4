package com.baiyi.opscloud.kubernetes.terminal.factory.impl;

import com.baiyi.opscloud.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsKubernetesConfig;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.factory.AbstractKubernetesTerminalProcess;
import com.baiyi.opscloud.kubernetes.terminal.factory.KubernetesTerminalProcessFactory;
import com.baiyi.opscloud.sshcore.base.ITerminalProcess;
import com.baiyi.opscloud.sshcore.builder.TerminalSessionInstanceBuilder;
import com.baiyi.opscloud.sshcore.enums.InstanceSessionTypeEnum;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.message.kubernetes.KubernetesLoginMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesResource;
import com.google.common.base.Joiner;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/7/15 10:10 上午
 * @Version 1.0
 */
@Component
public class KubernetesTerminalLoginProcess extends AbstractKubernetesTerminalProcess<KubernetesLoginMessage> implements ITerminalProcess {

    public interface SessionType {
        String CONTAINER_LOG = "CONTAINER_LOG";
        String CONTAINER_TERMINAL = "CONTAINER_TERMINAL";
    }

    /**
     * 登录
     *
     * @return
     */
    @Override
    public String getState() {
        return MessageState.LOGIN.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        KubernetesLoginMessage loginMessage = getMessage(message);
        heartbeat(terminalSession.getSessionId());
        KubernetesResource kubernetesResource = loginMessage.getData();
        kubernetesResource.getPods().forEach(pod ->
                pod.getContainers().forEach(container -> {
                    KubernetesDsInstanceConfig kubernetesDsInstanceConfig = buildConfig(kubernetesResource);
                    if (loginMessage.getSessionType().equals(SessionType.CONTAINER_LOG)) {
                        processLog(kubernetesDsInstanceConfig.getKubernetes(), terminalSession, kubernetesResource, pod, container);
                        return;
                    }
                    if (loginMessage.getSessionType().equals(SessionType.CONTAINER_TERMINAL)) {
                        processTerminal(kubernetesDsInstanceConfig.getKubernetes(), terminalSession, pod, container);
                        return;
                    }
                    // 会话类型不正确,直接关闭
                    KubernetesTerminalProcessFactory.getProcessByKey(MessageState.CLOSE.getState()).process(message, session, terminalSession);
                })
        );
    }

    private String toInstanceId(KubernetesResource.Pod pod, KubernetesResource.Container container) {
        return Joiner.on("#").join(pod.getName(), container.getName());
    }

    // 日志
    private void processLog(DsKubernetesConfig.Kubernetes kubernetes, TerminalSession terminalSession,
                            KubernetesResource kubernetesResource, KubernetesResource.Pod pod, KubernetesResource.Container container) {
        RemoteInvokeHandler.openKubernetesLog(
                terminalSession.getSessionId(),
                toInstanceId(pod, container),
                kubernetes,
                pod,
                container,
                kubernetesResource.getLines());
        simpleTerminalSessionFacade.recordTerminalSessionInstance(
                TerminalSessionInstanceBuilder.build(terminalSession.getSessionId(), pod, toInstanceId(pod, container),InstanceSessionTypeEnum.CONTAINER_LOG)
        );
    }

    // 终端
    private void processTerminal(DsKubernetesConfig.Kubernetes kubernetes, TerminalSession terminalSession, KubernetesResource.Pod pod, KubernetesResource.Container container) {
        RemoteInvokeHandler.openKubernetesTerminal(
                terminalSession.getSessionId(),
                toInstanceId(pod, container),
                kubernetes,
                pod,
                container);
        simpleTerminalSessionFacade.recordTerminalSessionInstance(
                TerminalSessionInstanceBuilder.build(terminalSession.getSessionId(), pod, toInstanceId(pod, container), InstanceSessionTypeEnum.CONTAINER_TERMINAL)
        );
    }


    @Override
    protected KubernetesLoginMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, KubernetesLoginMessage.class);
    }

}
