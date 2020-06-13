package com.baiyi.opscloud.cloud.server;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloud.server.factory.CloudServerFactory;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2020/1/10 12:39 下午
 * @Version 1.0
 */
public class ZabbixHostCloudserverTest extends BaseUnit {

    private static final String key = "ZabbixHostCloudserver";

    private ICloudServer getICloudServer() {
        return CloudServerFactory.getCloudServerByKey(key);
    }

    @Test
    void testUpdate() {
        getICloudServer().update("NULL", "13157");
    }

    @Test
    void testRsync() {
        getICloudServer().sync();
    }

}
