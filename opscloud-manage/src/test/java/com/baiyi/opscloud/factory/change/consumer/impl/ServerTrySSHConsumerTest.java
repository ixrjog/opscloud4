package com.baiyi.opscloud.factory.change.consumer.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.factory.change.consumer.util.RetryableSSH;
import org.junit.jupiter.api.Test;
import org.springframework.retry.annotation.EnableRetry;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/22 11:39 上午
 * @Version 1.0
 */
class ServerTrySSHConsumerTest extends BaseUnit {

    @Resource
    private RetryableSSH retryableSSH;

    @Test
    void tryServerSSH() throws Exception {
        retryableSSH.tryTest();
    }
}