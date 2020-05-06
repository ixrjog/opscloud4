package com.baiyi.opscloud.xterm.util;

import com.baiyi.opscloud.xterm.model.HostSystem;
import com.baiyi.opscloud.xterm.model.JSchSession;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import com.baiyi.opscloud.xterm.model.SessionOutput;
import com.baiyi.opscloud.xterm.task.SecureShellTask;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.UUID;

@Slf4j
public class SSHUtils {

    private static String appId = UUID.randomUUID().toString();
    private static String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEowIBAAKCAQEAwQ2INp7s3bb88/XW128mQS5W1Q59mZDqjIEREyeyjOZl8YdN\n" +
            "ZhXArDxkYJo8XTa44kCH2tx+iHQlVT7K8dQXcLCmH1Q5PDxbhFq1T1rEue7f2Ftx\n" +
            "FwQnli7RnPRB/0ENt+Azmz1YZjtZm5aIwIbeNW5Z5Jh3Z5GDzccU\n" +
            "-----END RSA PRIVATE KEY-----";
    private static String publicKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDBDYg2nuzdtvzz9dbXbyZBLlbVDn2ZkOqMgRETJ7KM5mXxh01mFcCsPGRgmjxdNrjiQIfa3H6IdCVVPsrx1BdwsKYfVDk8PFuEWrVPWsS57t/YW3GME3iHGOWKZ2M4aK/9j77GA7DLy0ndL1Y188H4IyFfnEbkMo9AEFh5fAIWjTNG5KynHbYa1slVWX1cyhzEYJYtmJzLNAd3Uv4MJTeiuYblB9FqxF3X+HbOaeYZGpaltseDIi4siFjToGyNbqLaxN7sUulibEOHVN8p2LkeBX4a4db9GDySZB9DYNvsULhVrCt1a7TdDhodAzpnQw9NKTm/ZeTrLgO7chpttxEZ zxxiao@zxxiao";
    private static String passphrase = "";

    private static final int SERVER_ALIVE_INTERVAL = 60 * 1000;
    public static final int SESSION_TIMEOUT = 60000;
    public static final int CHANNEL_TIMEOUT = 60000;

    public static void openSSHTermOnSystem(String sessionId, String instanceId, HostSystem hostSystem) {
        JSch jsch = new JSch();

        hostSystem.setStatusCd(HostSystem.SUCCESS_STATUS);
        hostSystem.setInstanceId(instanceId);

        try {
            //add private key
            jsch.addIdentity(appId, privateKey.trim().getBytes(), publicKey.getBytes(), passphrase.getBytes());

            //create session
            Session session = jsch.getSession("xiaozx", "oc.ops.yangege.cn", 22);

//            //set password if it exists
//            if (password != null && !password.trim().equals("")) {
//                session.setPassword(password);
//            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setServerAliveInterval(SERVER_ALIVE_INTERVAL);
            session.connect(SESSION_TIMEOUT);
            Channel channel = session.openChannel("shell");
            ((ChannelShell) channel).setPtyType("xterm");

            InputStream outFromChannel = channel.getInputStream();


            //new session output
            SessionOutput sessionOutput = new SessionOutput(sessionId, hostSystem);

            Runnable run = new SecureShellTask(sessionOutput, outFromChannel);
            Thread thread = new Thread(run);
            thread.start();


            OutputStream inputToChannel = channel.getOutputStream();
            PrintStream commander = new PrintStream(inputToChannel, true);

            JSchSession jSchSession = new JSchSession();
            jSchSession.setSessionId(sessionId);
            jSchSession.setInstanceId(instanceId);
            jSchSession.setCommander(commander);
            JSchSessionMap.addSession(jSchSession);

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
}
