package com.baiyi.opscloud.datasource.other;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.LXHLConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.message.LXHLMessageResponse;
import com.baiyi.opscloud.datasource.message.driver.LXHLMessageDriver;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author 修远
 * @Date 2022/8/31 7:47 PM
 * @Since 1.0
 */
public class LXHLTest extends BaseUnit {

    @Resource
    private DsConfigManager dsConfigManager;

    @Resource
    private LXHLMessageDriver lxhlMessageDriver;

    // 45 sms  46 vms
    private LXHLConfig getConfigById(int id) {
        return dsConfigManager.build(dsConfigManager.getConfigById(id), LXHLConfig.class);
    }

    @Test
    void xxx() {
        LXHLConfig config = getConfigById(46);
        LXHLMessageResponse.SendMessage sendMessage = lxhlMessageDriver.sendMessage(config.getAccount(), "15067127069", "测试消息123", "PalmPay");
        print(sendMessage);
        print(sendMessage.getSuccess());
    }
}
