package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.HostUtil;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author baiyi
 * @Date 2022/12/26 14:11
 * @Version 1.0
 */
public class HostUtilTest extends BaseUnit {

    @Test
    void test() {
        try {
            InetAddress inetAddress = HostUtil.getInetAddress();
            print(inetAddress.getHostName());
            print(inetAddress.getHostAddress());
            print(inetAddress.getCanonicalHostName());
        } catch (UnknownHostException e) {
        }
    }


}
