package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.domain.generator.opscloud.OcInstance;

import java.net.InetAddress;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/24 5:29 下午
 * @Since 1.0
 */
public class OpscloudInstanceBuilder {

    public static OcInstance build(InetAddress inetAddress) {
        OcInstance ocInstance = new OcInstance();
        ocInstance.setHostIp(inetAddress.getHostAddress());
        ocInstance.setHostname(inetAddress.getHostName());
        ocInstance.setName(inetAddress.getCanonicalHostName());
        ocInstance.setInstanceStatus(0);
        ocInstance.setIsActive(true);
        return ocInstance;
    }
}
