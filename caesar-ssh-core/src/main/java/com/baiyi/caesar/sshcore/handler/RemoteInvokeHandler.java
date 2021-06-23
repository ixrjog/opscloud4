package com.baiyi.caesar.sshcore.handler;

import com.baiyi.caesar.domain.generator.caesar.Credential;
import com.baiyi.caesar.sshcore.message.BaseMessage;
import com.baiyi.caesar.sshcore.model.*;
import com.baiyi.caesar.sshcore.task.terminal.WatchOutputTask;
import com.baiyi.caesar.sshcore.util.ChannelShellUtil;
import com.baiyi.caesar.sshcore.util.SessionConfigUtil;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
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

    private static String appId = UUID.randomUUID().toString();

    private static final int SERVER_ALIVE_INTERVAL = 60 * 1000;
    public static final int SESSION_TIMEOUT = 60000;
    public static final int CHANNEL_TIMEOUT = 60000;

    private static void invokeSshCredential(HostSystem hostSystem, JSch jsch, Session session) throws JSchException {
        Credential credential = hostSystem.getSshCredential().getCredential();
        // 按凭证类型
        switch (hostSystem.getSshCredential().getCredential().getKind()) {
            case 1: // password
                session.setPassword(credential.getCredential());
                break;
            case 2:   // prvKey
                jsch.addIdentity(credential.getCredential(), credential.getPassphrase().getBytes());
                break;
            case 3:  // prvKey + pubKey
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
    public static void openSSHTermOnSystemForWebTerminal(String sessionId, String instanceId, HostSystem hostSystem) {
        JSch jsch = new JSch();

        hostSystem.setStatusCd(HostSystem.SUCCESS_STATUS);
        hostSystem.setInstanceId(instanceId);

        try {
            if (hostSystem.getSshCredential() == null) return;
            Session session = jsch.getSession(hostSystem.getSshCredential().getServerAccount().getUsername(), hostSystem.getHost(),
                    hostSystem.getPort() == null ? 22 : hostSystem.getPort());
            invokeSshCredential(hostSystem, jsch, session);
            SessionConfigUtil.setDefault(session); // 默认设置
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            ChannelShellUtil.setDefault(channel);

            setChannelPtySize(channel, hostSystem.getLoginMessage());

            //setChannelPtySize(channel, hostSystem.getLoginMessage());

            // new session output
            SessionOutput sessionOutput = new SessionOutput(sessionId, hostSystem);
            // 启动线程处理会话
            Runnable run = new WatchOutputTask(sessionOutput, channel.getInputStream());
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
    public static void openSSHTermOnSystemForSSHServer(String sessionId,  HostSystem hostSystem) {
        JSch jsch = new JSch();

        hostSystem.setStatusCd(HostSystem.SUCCESS_STATUS);


        try {
            if (hostSystem.getSshCredential() == null) return;
            Session session = jsch.getSession(hostSystem.getSshCredential().getServerAccount().getUsername(), hostSystem.getHost(),
                    hostSystem.getPort() == null ? 22 : hostSystem.getPort());
            invokeSshCredential(hostSystem, jsch, session);
            SessionConfigUtil.setDefault(session); // 默认设置
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            ChannelShellUtil.setDefault(channel);
            setChannelPtySize(channel, hostSystem.getTerminalSize());


            // new session output
            SessionOutput sessionOutput = new SessionOutput(sessionId, hostSystem);
            // 启动线程处理会话
            Runnable run = new WatchOutputTask(sessionOutput, channel.getInputStream());
            Thread thread = new Thread(run);
            thread.start();

            OutputStream inputToChannel = channel.getOutputStream();

            JSchSession jSchSession = JSchSession.builder()
                    .sessionId(sessionId)
                    .instanceId(hostSystem.getInstanceId())
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

    public static void setChannelPtySize(ChannelShell channel, BaseMessage baseMessage) {
        int width = baseMessage.getTerminal().getWidth();
        int height = baseMessage.getTerminal().getHeight();
        // int cols = (int) Math.floor(width / 7.2981);
        int cols = (int) Math.floor(width / 7.0);
        int rows = (int) Math.floor(height / 14.4166);
        channel.setPtySize(cols, rows, width, height);
    }

    public static void setChannelPtySize(ChannelShell channel, Size size) {
        channel.setPtySize(size.getColumns(), size.getRows(), size.getColumns() * 7, (int) Math.floor(size.getRows() / 14.4166));
    }
}
