package com.baiyi.opscloud.kubernetes.terminal.handler.impl;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.factory.KubernetesTerminalMessageHandlerFactory;
import com.baiyi.opscloud.kubernetes.terminal.handler.AbstractKubernetesTerminalMessageHandler;
import com.baiyi.opscloud.sshcore.ITerminalMessageHandler;
import com.baiyi.opscloud.sshcore.builder.TerminalSessionInstanceBuilder;
import com.baiyi.opscloud.sshcore.enums.InstanceSessionTypeEnum;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesResource;
import com.google.common.base.Joiner;
import com.google.gson.GsonBuilder;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/7/15 10:10 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesTerminalLoginHandler extends AbstractKubernetesTerminalMessageHandler<KubernetesMessage.Login>
        implements ITerminalMessageHandler {

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
    public void handle(String message, Session session, TerminalSession terminalSession) {
        try {
            String username = SessionHolder.getUsername();
            KubernetesMessage.Login loginMessage = toMessage(message);
            heartbeat(terminalSession.getSessionId());
            KubernetesResource kubernetesResource = loginMessage.getData();
            kubernetesResource.getPods().forEach(pod ->
                    // JDK21 VirtualThreads
                    pod.getContainers().forEach(container -> Thread.ofVirtual().start(() -> {
                        SessionHolder.setUsername(username);
                        log.info("初始化容器终端: sessionType={}, container={}", loginMessage.getSessionType(), container.getName());
                        KubernetesConfig kubernetesDsInstanceConfig = buildConfig(kubernetesResource);
                        if (loginMessage.getSessionType().equals(SessionType.CONTAINER_LOG)) {
                            processLog(kubernetesDsInstanceConfig.getKubernetes(), terminalSession, kubernetesResource, pod, container);
                            return;
                        }
                        if (loginMessage.getSessionType().equals(SessionType.CONTAINER_TERMINAL)) {
                            processTerminal(kubernetesDsInstanceConfig.getKubernetes(), terminalSession, pod, container);
                            return;
                        }
                        // 会话类型不正确,直接关闭
                        KubernetesTerminalMessageHandlerFactory.getHandlerByState(MessageState.CLOSE.getState()).handle(message, session, terminalSession);
                    }))
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String toInstanceId(KubernetesResource.Pod pod, KubernetesResource.Container container) {
        return Joiner.on("#").join(pod.getName(), container.getName());
    }

    /**
     * 日志
     *
     * @param kubernetes
     * @param terminalSession
     * @param kubernetesResource
     * @param pod
     * @param container
     */
    private void processLog(KubernetesConfig.Kubernetes kubernetes, TerminalSession terminalSession,
                            KubernetesResource kubernetesResource, KubernetesResource.Pod pod, KubernetesResource.Container container) {
        RemoteInvokeHandler.openKubernetesLog(
                terminalSession.getSessionId(),
                toInstanceId(pod, container),
                kubernetes,
                pod,
                container,
                kubernetesResource.getLines());
        simpleTerminalSessionFacade.recordTerminalSessionInstance(
                TerminalSessionInstanceBuilder.build(terminalSession.getSessionId(), pod, toInstanceId(pod, container), InstanceSessionTypeEnum.CONTAINER_LOG)
        );
    }

    /**
     * 终端
     *
     * @param kubernetes
     * @param terminalSession
     * @param pod
     * @param container
     */
    private void processTerminal(KubernetesConfig.Kubernetes kubernetes, TerminalSession terminalSession, KubernetesResource.Pod pod, KubernetesResource.Container container) {
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
    protected KubernetesMessage.Login toMessage(String message) {
        return new GsonBuilder().create().fromJson(message, KubernetesMessage.Login.class);
    }

}