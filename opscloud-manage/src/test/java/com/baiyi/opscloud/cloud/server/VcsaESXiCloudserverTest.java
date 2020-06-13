package com.baiyi.opscloud.cloud.server;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloud.server.factory.CloudServerFactory;
import com.baiyi.opscloud.vmware.vcsa.esxi.VcsaESXi;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/15 3:25 下午
 * @Version 1.0
 */
public class VcsaESXiCloudserverTest extends BaseUnit {

    @Resource
    private VcsaESXi vcsaESXi;

    private static final String key = "VcsaESXiCloudserver";

    private ICloudServer getICloudServer() {
        return CloudServerFactory.getCloudServerByKey(key);
    }

    @Test
    void testRsync() {
        getICloudServer().sync();
    }

    @Test
    void testUpdate() {
        getICloudServer().update("cn-hangzhou", "i-bp18mr52zr792unc1kxd");
    }


}
