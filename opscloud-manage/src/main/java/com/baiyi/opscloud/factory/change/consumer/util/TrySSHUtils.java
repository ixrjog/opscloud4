package com.baiyi.opscloud.factory.change.consumer.util;

import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2020/7/18 10:42 上午
 * @Version 1.0
 */
@Slf4j
public class TrySSHUtils {

    private static String appId = UUID.randomUUID().toString();

    private static final int SERVER_ALIVE_INTERVAL = 60 * 1000;
    public static final int SESSION_TIMEOUT = 5000;

    public static void trySSH(OcServer ocServer, SSHKeyCredential sshKeyCredential) throws RuntimeException {
        JSch jsch = new JSch();
        try {
            jsch.addIdentity(appId, sshKeyCredential.getPrivateKey().trim().getBytes(), sshKeyCredential.getPublicKey().getBytes(), sshKeyCredential.getPassphrase().getBytes());
            //create session
            Session session = jsch.getSession(sshKeyCredential.getSystemUser(), ocServer.getPrivateIp(),
                    22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setServerAliveInterval(SERVER_ALIVE_INTERVAL);
            session.connect(SESSION_TIMEOUT);
            log.info("Try server ip = {} ssh success !", ocServer.getPrivateIp());
            // 这2行不加也可以
            Channel channel = session.openChannel("shell");
            channel.connect();
            session.disconnect();
        } catch (JSchException e) {
            log.info("Try server ip = {} ssh failed !", ocServer.getPrivateIp());
            log.info(e.getMessage());
            throw new RuntimeException();
        }
    }


}
