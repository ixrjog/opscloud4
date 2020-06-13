package com.baiyi.opscloud.cloud.server;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloud.server.factory.CloudServerFactory;
import com.baiyi.opscloud.vmware.vcsa.vm.VcsaVM;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/15 4:21 下午
 * @Version 1.0
 */
public class VcsaVMCloudserverTest extends BaseUnit {

    @Resource
    private VcsaVM vcsaVM;

    private static final String key = "VcsaVMCloudServer";

    private ICloudServer getICloudServer() {
        return CloudServerFactory.getCloudServerByKey(key);
    }

    @Test
    void testRsync() {
        getICloudServer().sync();
    }

    @Test
    void testUpdate() {
        getICloudServer().update("NULL", "i-bp18mr52zr792unc1kxd");
    }
}
