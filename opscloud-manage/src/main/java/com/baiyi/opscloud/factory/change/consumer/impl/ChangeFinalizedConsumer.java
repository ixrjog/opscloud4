package com.baiyi.opscloud.factory.change.consumer.impl;

import com.baiyi.opscloud.common.base.ServerChangeFlow;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.factory.change.consumer.IServerChangeConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/6/1 2:50 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ChangeFinalizedConsumer extends BaseServerChangeConsumer implements IServerChangeConsumer {

    @Override
    public String getKey() {
        return ServerChangeFlow.FINALIZED.getName();
    }

    @Override
    public BusinessWrapper<Boolean> consuming(OcServerChangeTask ocServerChangeTask, OcServerChangeTaskFlow
            ocServerChangeTaskFlow) {
        ocServerChangeTask.setResultCode(0);
        ocServerChangeTask.setResultMsg("SUCCESS");
        ocServerChangeTask.setEndTime(new Date());
        ocServerChangeTask.setTaskStatus(0);
        saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow);
        ocServerChangeTaskService.updateOcServerChangeTask(ocServerChangeTask);
        return BusinessWrapper.SUCCESS;
    }


}
