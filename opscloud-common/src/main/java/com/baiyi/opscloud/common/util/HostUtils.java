package com.baiyi.opscloud.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/24 5:25 下午
 * @Since 1.0
 */
public class HostUtils {

    public static String getHostAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }
}
