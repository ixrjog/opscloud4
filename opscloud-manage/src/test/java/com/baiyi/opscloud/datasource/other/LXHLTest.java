package com.baiyi.opscloud.datasource.other;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.LXHLConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.message.LXHLMessageResponse;
import com.baiyi.opscloud.datasource.message.driver.LXHLMessageDriver;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author 修远
 * @Date 2022/8/31 7:47 PM
 * @Since 1.0
 */
public class LXHLTest extends BaseUnit {

    @Resource
    private DsConfigHelper dsConfigHelper;

    @Resource
    private LXHLMessageDriver lxhlMessageDriver;

    // 45 sms  46 vms
    private LXHLConfig getConfigById(int id) {
        return dsConfigHelper.build(dsConfigHelper.getConfigById(id), LXHLConfig.class);
    }

    @Test
    void xxx() {
        LXHLConfig config = getConfigById(45);
        LXHLMessageResponse.SendMessage sendMessage = lxhlMessageDriver.sendMessage(config.getAccount(), "15067127069,13456768044", "测试消息123", "PalmPay");
        print(sendMessage);
    }
}
