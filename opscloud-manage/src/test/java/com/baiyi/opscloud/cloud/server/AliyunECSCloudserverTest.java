package com.baiyi.opscloud.cloud.server;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.aliyun.ecs.AliyunECS;
import com.baiyi.opscloud.aliyun.ecs.base.ECSDisk;
import com.baiyi.opscloud.cloud.server.factory.CloudServerFactory;
import com.baiyi.opscloud.common.util.JSONUtils;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 11:20 下午
 * @Version 1.0
 */
public class AliyunECSCloudserverTest extends BaseUnit {

    @Resource
    private AliyunECS aliyunECS;

    private static final String key = "AliyunECSCloudServer";

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

    @Test
    void testGetDiskList() {
        List<ECSDisk> diskList = aliyunECS.getDiskList("cn-hangzhou", "i-bp18mr52zr792unc1kxd");
        for (ECSDisk disk : diskList) {
            System.err.println(JSONUtils.writeValueAsString(disk));
        }
    }

    @Test
    void aTest() {
        aliyunECS.modifyInstanceChargeType("cn-hangzhou", "i-bp1fjdhecwn46fbz4k1x", "PostPaid");
    }
}
