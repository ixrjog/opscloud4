package com.baiyi.caesar.sshcore.util;

import com.jcraft.jsch.ChannelShell;

/**
 * @Author baiyi
 * @Date 2021/6/3 9:59 上午
 * @Version 1.0
 */
public class ChannelShellUtil {

    public static void setDefault(ChannelShell channel) {
        channel.setEnv("LANG", "en_US.UTF-8");
        // SSH 代理转发
        channel.setAgentForwarding(false);
        channel.setPtyType("xterm");
    }
}
