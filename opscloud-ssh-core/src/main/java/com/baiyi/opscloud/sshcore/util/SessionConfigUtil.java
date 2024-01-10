package com.baiyi.opscloud.sshcore.util;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @Author baiyi
 * @Date 2021/6/3 9:52 上午
 * @Version 1.0
 */
public class SessionConfigUtil {

    private static final int SERVER_ALIVE_INTERVAL = 60 * 1000;
    public static final int SESSION_TIMEOUT = 60000;
    public static final int CHANNEL_TIMEOUT = 60000;

    public interface Configs {
        String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
        String PREFERRED_AUTHENTICATIONS = "PreferredAuthentications";
    }

    public static void setDefault(Session session) throws JSchException {
        session.setConfig(Configs.STRICT_HOST_KEY_CHECKING, "no");
        session.setConfig(Configs.PREFERRED_AUTHENTICATIONS, "publickey,keyboard-interactive,password");
        session.setServerAliveInterval(SERVER_ALIVE_INTERVAL);
        session.connect(SESSION_TIMEOUT);
        session.setTimeout(CHANNEL_TIMEOUT);
    }

}