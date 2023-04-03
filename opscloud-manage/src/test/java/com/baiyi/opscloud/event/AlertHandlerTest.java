package com.baiyi.opscloud.event;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.event.alert.AlertHandler;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/5/31 21:34
 * @Version 1.0
 */
public class AlertHandlerTest extends BaseUnit {

    @Resource
    private AlertHandler alertHandler;

    @Test
    void sendTaskTest() {
        alertHandler.sendTask();
    }

}
