package com.baiyi.opscloud.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author baiyi
 * @Date 2020/9/7 9:25 上午
 * @Version 1.0
 */
public class HostUtil {

    private HostUtil() {
    }

    public static String getHostAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public static String getHostName() {
        try {
            return getInetAddress().getCanonicalHostName();
        } catch (UnknownHostException e) {
            return "UnknownHost";
        }
    }

}