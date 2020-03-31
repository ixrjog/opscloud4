package com.baiyi.opscloud.cloud.server;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloud.server.factory.CloudServerFactory;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2020/1/13 6:03 下午
 * @Version 1.0
 */
public class AwsEC2CloudserverTest  extends BaseUnit {

    private static final String key = "AwsEC2Cloudserver";

    private ICloudServer getICloudServer() {
        return CloudServerFactory.getCloudServerByKey(key);
    }

    @Test
    void testUpdate() {
        getICloudServer().update("NULL", "i-0553886d8d43a3ea1");
    }

    @Test
    void testSync() {
        getICloudServer().sync();
    }


}
