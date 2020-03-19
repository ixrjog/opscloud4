package com.baiyi.opscloud.cloud.vpc;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloud.vpc.factory.CloudVPCFactory;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2020/1/10 11:20 下午
 * @Version 1.0
 */
public class AliyunCloudVPCTest extends BaseUnit {

    private static final String key = "AliyunCloudVPC";

    private ICloudVPC getICloudVPC() {
        return CloudVPCFactory.getCloudVPCByKey(key);
    }

    @Test
    void testSyncVPC() {
        getICloudVPC().syncVPC();
    }


}
