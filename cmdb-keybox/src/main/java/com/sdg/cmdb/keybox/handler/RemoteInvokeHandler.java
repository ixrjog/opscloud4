package com.sdg.cmdb.keybox.handler;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sdg.cmdb.domain.keybox.ApplicationKeyDO;
import com.sdg.cmdb.domain.keybox.HostSystem;
import com.sdg.cmdb.util.EncryptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zxxiao on 2016/12/1.
 */
public class RemoteInvokeHandler {

    private static final Logger logger = LoggerFactory.getLogger(RemoteInvokeHandler.class);

    public static final int SERVER_ALIVE_INTERVAL = 3600 * 1000;
    public static final int SESSION_TIMEOUT = 6000;

    public static ConnectionSession getSession(ApplicationKeyDO applicationKeyDO, HostSystem hostSystem) {
        JSch jSch = new JSch();

        String passphrase = applicationKeyDO.getPassphrase();
        if (StringUtils.isEmpty(passphrase)) {
            passphrase = "";
        }
        try {
            jSch.addIdentity(applicationKeyDO.getSessionId() + "",
                    EncryptionUtil.decrypt(applicationKeyDO.getPrivateKey()).getBytes(),
                    applicationKeyDO.getPublicKey().getBytes(),
                    passphrase.getBytes());

            Session session = jSch.getSession(hostSystem.getUser(), hostSystem.getHost(),
                    hostSystem.getPort() == null ? 22 : hostSystem.getPort());
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setServerAliveInterval(SERVER_ALIVE_INTERVAL);
            session.connect(SESSION_TIMEOUT);

            Channel channel = session.openChannel("shell");
            ((ChannelShell) channel).setAgentForwarding(true);
            ((ChannelShell) channel).setPtyType("xterm");

            channel.connect();

            ConnectionSession connectionSession = new ConnectionSession(channel, session);

            return connectionSession;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            hostSystem.setErrorMsg(e.getMessage());
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
        return null;
    }
}
