package com.baiyi.opscloud.sshcore.handler;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.sshcore.model.*;
import com.baiyi.opscloud.sshcore.task.kubernetes.WatchKubernetesTerminalOutputTask;
import com.baiyi.opscloud.sshcore.task.ssh.WatchSshServerOutputTask;
import com.baiyi.opscloud.sshcore.task.terminal.WatchWebTerminalOutputTask;
import com.baiyi.opscloud.sshcore.util.ChannelShellUtil;
import com.baiyi.opscloud.sshcore.util.SessionConfigUtil;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jline.terminal.Size;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:05 上午
 * @Version 1.0
 */
@Slf4j
public class RemoteInvokeHandler {

    private static final int SSH_PORT = 22;

    private static final String appId = UUID.randomUUID().toString();

//    private static final int SERVER_ALIVE_INTERVAL = 60 * 1000;
//    public static final int SESSION_TIMEOUT = 60000;
//    public static final int CHANNEL_TIMEOUT = 60000;

    /**
     * 按类型注入凭据
     *
     * @param hostSystem
     * @param jsch
     * @param session
     * @throws JSchException
     */
    private static void invokeSshCredential(HostSystem hostSystem, JSch jsch, Session session) throws JSchException {
        Credential credential = hostSystem.getSshCredential().getCredential();
        // 按凭证类型
        switch (hostSystem.getSshCredential().getCredential().getKind()) {
            case 1: // password
                session.setPassword(credential.getCredential());
                break;
            case 2: // prvKey
                jsch.addIdentity(credential.getCredential(), credential.getPassphrase().getBytes());
                break;
            case 3: // prvKey + pubKey
                jsch.addIdentity(appId, credential.getCredential().trim().getBytes(), credential.getCredential2().getBytes(), credential.getPassphrase().getBytes());
                break;
        }
    }

    /**
     * Web Terminal
     *
     * @param sessionId
     * @param instanceId
     * @param hostSystem
     */
    public static void openWebTerminal(String sessionId, String instanceId, HostSystem hostSystem) {
        JSch jsch = new JSch();

        hostSystem.setStatusCd(HostSystem.SUCCESS_STATUS);
        hostSystem.setInstanceId(instanceId);
        // TODO
        try {
            if (hostSystem.getSshCredential() == null) return;
            Session session = jsch.getSession(hostSystem.getSshCredential().getServerAccount().getUsername(), hostSystem.getHost(),
                    hostSystem.getPort() == null ? SSH_PORT : hostSystem.getPort());
            invokeSshCredential(hostSystem, jsch, session);
            SessionConfigUtil.setDefault(session); // 默认设置
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            ChannelShellUtil.setDefault(channel);

            setChannelPtySize(channel, hostSystem.getLoginMessage());

            // new session output
            SessionOutput sessionOutput = new SessionOutput(sessionId, hostSystem);
            // 启动线程处理会话
            Runnable run = new WatchWebTerminalOutputTask(sessionOutput, channel.getInputStream());
            Thread thread = new Thread(run);
            thread.start();

            OutputStream inputToChannel = channel.getOutputStream();

            JSchSession jSchSession = JSchSession.builder()
                    .sessionId(sessionId)
                    .instanceId(instanceId)
                    .commander(new PrintStream(inputToChannel, true))
                    .inputToChannel(inputToChannel)
                    .channel(channel)
                    .hostSystem(hostSystem)
                    .build();
            jSchSession.setSessionOutput(sessionOutput);
            JSchSessionContainer.addSession(jSchSession);

            channel.connect();
        } catch (Exception e) {
            log.info(e.toString(), e);
            if (e.getMessage().toLowerCase().contains("userauth fail")) {
                hostSystem.setStatusCd(HostSystem.PUBLIC_KEY_FAIL_STATUS);
            } else if (e.getMessage().toLowerCase().contains("auth fail") || e.getMessage().toLowerCase().contains("auth cancel")) {
                hostSystem.setStatusCd(HostSystem.AUTH_FAIL_STATUS);
            } else if (e.getMessage().toLowerCase().contains("unknownhostexception")) {
                hostSystem.setErrorMsg("DNS Lookup Failed");
                hostSystem.setStatusCd(HostSystem.HOST_FAIL_STATUS);
            } else {
                hostSystem.setStatusCd(HostSystem.GENERIC_FAIL_STATUS);
            }
        }
    }

    /**
     * Web Terminal
     *
     * @param sessionId
     * @param hostSystem
     */
    public static void openSSHServer(String sessionId, HostSystem hostSystem, OutputStream out) {
        JSch jsch = new JSch();
        hostSystem.setStatusCd(HostSystem.SUCCESS_STATUS);
        try {
            if (hostSystem.getSshCredential() == null) return;
            Session session = jsch.getSession(hostSystem.getSshCredential().getServerAccount().getUsername(), hostSystem.getHost(),
                    hostSystem.getPort() == null ? 22 : hostSystem.getPort());
            invokeSshCredential(hostSystem, jsch, session);
            SessionConfigUtil.setDefault(session); // 默认设置
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            // channel.setPtyType("dumb");
            ChannelShellUtil.setDefault(channel);
            setChannelPtySize(channel, hostSystem.getTerminalSize());
            // new session output
            SessionOutput sessionOutput = new SessionOutput(sessionId, hostSystem);
            // 启动线程处理会话
            Runnable run = new WatchSshServerOutputTask(sessionOutput, channel.getInputStream(), out);
            Thread thread = new Thread(run);
            thread.start();
            /////////////////////
            OutputStream inputToChannel = channel.getOutputStream();
            JSchSession jSchSession = JSchSession.builder()
                    .sessionId(sessionId)
                    .instanceId(hostSystem.getInstanceId())
                    // StandardCharsets.UTF_8.name()
                    .commander(new PrintStream(inputToChannel, true))
                    .inputToChannel(inputToChannel)
                    .channel(channel)
                    .hostSystem(hostSystem)
                    .build();
            jSchSession.setSessionOutput(sessionOutput);
            JSchSessionContainer.addSession(jSchSession);
            channel.connect();
        } catch (Exception e) {
            log.info(e.toString(), e);
            if (e.getMessage().toLowerCase().contains("userauth fail")) {
                hostSystem.setStatusCd(HostSystem.PUBLIC_KEY_FAIL_STATUS);
            } else if (e.getMessage().toLowerCase().contains("auth fail") || e.getMessage().toLowerCase().contains("auth cancel")) {
                hostSystem.setStatusCd(HostSystem.AUTH_FAIL_STATUS);
            } else if (e.getMessage().toLowerCase().contains("unknownhostexception")) {
                hostSystem.setErrorMsg("DNS Lookup Failed");
                hostSystem.setStatusCd(HostSystem.HOST_FAIL_STATUS);
            } else {
                hostSystem.setStatusCd(HostSystem.GENERIC_FAIL_STATUS);
            }
        }
    }

    public static void openKubernetesLog(String sessionId, String instanceId, KubernetesConfig.Kubernetes kubernetes,
                                         KubernetesResource.Pod pod, KubernetesResource.Container container, Integer lines) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LogWatch logWatch = KubernetesPodDriver.getPodLogWatch(kubernetes,
                pod.getNamespace(),
                pod.getName(),
                container.getName(),
                lines, out);
        SessionOutput sessionOutput = new SessionOutput(sessionId, instanceId);
        // 启动线程处理会话
        WatchKubernetesTerminalOutputTask run = new WatchKubernetesTerminalOutputTask(sessionOutput, out);
        Thread thread = new Thread(run);
        thread.start();

        KubernetesSession kubernetesSession = KubernetesSession.builder()
                .sessionId(sessionId)
                .instanceId(instanceId)
                .logWatch(logWatch)
                .watchKubernetesTerminalOutputTask(run)
                .build();
        kubernetesSession.setSessionOutput(sessionOutput);
        KubernetesSessionContainer.addSession(kubernetesSession);
    }

    public static void openKubernetesTerminal(String sessionId, String instanceId, KubernetesConfig.Kubernetes kubernetes,
                                              KubernetesResource.Pod pod, KubernetesResource.Container container) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        KubernetesPodDriver.SimpleListener listener = new KubernetesPodDriver.SimpleListener();
        ExecWatch execWatch = KubernetesPodDriver.loginPodContainer(
                kubernetes,
                pod.getNamespace(),
                pod.getName(),
                container.getName(),
                listener,
                out);
        SessionOutput sessionOutput = new SessionOutput(sessionId, instanceId);
        // 启动线程处理会话
        WatchKubernetesTerminalOutputTask run = new WatchKubernetesTerminalOutputTask(sessionOutput, out);
        Thread thread = new Thread(run);
        thread.start();
        KubernetesSession kubernetesSession = KubernetesSession.builder()
                .sessionId(sessionId)
                .instanceId(instanceId)
                .execWatch(execWatch)
                .watchKubernetesTerminalOutputTask(run)
                .build();
        kubernetesSession.setSessionOutput(sessionOutput);
        KubernetesSessionContainer.addSession(kubernetesSession);
    }

    public static void setChannelPtySize(ChannelShell channel, ServerMessage.BaseMessage message) {
        if (channel == null || channel.isClosed()) return;
        channel.setPtySize(message.getCols(), message.getRows(), message.getWidth(), message.getHeight());
    }

    public static void setChannelPtySize(ChannelShell channel, Size size) {
        if (channel == null || channel.isClosed()) return;
        channel.setPtySize(size.getColumns(), size.getRows(), size.getColumns() * 7, (int) Math.floor(size.getRows() / 14.4166));
    }

}
