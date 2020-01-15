package com.baiyi.opscloud.cloudserver;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloudserver.factory.CloudserverFactory;
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

    private static final String key = "VcsaVMCloudserver";

    private ICloudserver getICloudserver() {
        return CloudserverFactory.getCloudserverByKey(key);
    }

    @Test
    void testRsync() {
        getICloudserver().sync();
    }

    @Test
    void testUpdate() {
        getICloudserver().update("NULL", "i-bp18mr52zr792unc1kxd");
    }
}
