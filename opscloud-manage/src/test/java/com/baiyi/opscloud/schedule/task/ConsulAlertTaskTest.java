package com.baiyi.opscloud.schedule.task;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.annotation.WatchTask;
import com.baiyi.opscloud.facade.task.ConsulAlertFacade;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2023/2/24 15:13
 * @Version 1.0
 */
class ConsulAlertTaskTest extends BaseUnit {

    @Resource
    private ConsulAlertFacade consulAlertFacade;

    @WatchTask(name = "测试任务")
    @Test
    void testTask() {
        consulAlertFacade.ruleEvaluate();
    }

}