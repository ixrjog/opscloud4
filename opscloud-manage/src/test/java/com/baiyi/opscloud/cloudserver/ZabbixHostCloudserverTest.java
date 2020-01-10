package com.baiyi.opscloud.cloudserver;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloudserver.factory.CloudserverFactory;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2020/1/10 12:39 下午
 * @Version 1.0
 */
public class ZabbixHostCloudserverTest extends BaseUnit {

    private static final String key = "ZabbixHostCloudserver";

    private Cloudserver getCloudserver() {
        return CloudserverFactory.getCloudserverByKey(key);
    }

    @Test
    void testRsync() {
        getCloudserver().sync();
    }

}
