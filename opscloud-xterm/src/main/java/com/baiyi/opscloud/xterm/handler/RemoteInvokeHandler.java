package com.baiyi.opscloud.xterm.handler;

import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.xterm.message.XTermInitialWSMessage;
import com.baiyi.opscloud.xterm.model.HostSystem;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;

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

    public static ConnectionSession getSession( String instanceId, HostSystem hostSystem) {
        JSch jsch = new JSch();

        hostSystem.setStatusCd(HostSystem.SUCCESS_STATUS);
        hostSystem.setInstanceId(instanceId);

        try {
            SSHKeyCredential sshKeyCredential = hostSystem.getSshKeyCredential();
            jsch.addIdentity(appId, sshKeyCredential.getPrivateKey().trim().getBytes(), sshKeyCredential.getPublicKey().getBytes(), sshKeyCredential.getPassphrase().getBytes());

            //create session
            Session session = jsch.getSession(sshKeyCredential.getSystemUser(), hostSystem.getHost(),
                    hostSystem.getPort() == null ? 22 : hostSystem.getPort());
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setServerAliveInterval(SERVER_ALIVE_INTERVAL);
            session.connect(SESSION_TIMEOUT);

            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            channel.setEnv("LANG", "en_US.UTF-8");
            // SSH 代理转发
            channel.setAgentForwarding(false);
            channel.setPtyType("xterm");

            invokeChannelPtySize(channel, hostSystem.getInitialMessage());

            channel.connect();
            ConnectionSession connectionSession = new ConnectionSession(channel, session);

            return connectionSession;

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
            return null;
        }
    }

    public static void invokeChannelPtySize(ChannelShell channel, XTermInitialWSMessage initialMessage) {
        int width = initialMessage.getXtermWidth();
        int height = initialMessage.getXtermHeight();
        channel.setPtySize((int) Math.floor(width / 7.2981), (int) Math.floor(height / 14.4166), width, height);
    }
}
