package com.baiyi.opscloud.factory.change.consumer.impl;

import com.baiyi.opscloud.common.base.ServerChangeFlow;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.factory.change.consumer.IServerChangeConsumer;
import com.baiyi.opscloud.factory.change.consumer.bo.ChangeResult;
import com.baiyi.opscloud.server.ServerCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/29 3:09 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ServerFactoryUnactiveConsumer extends BaseServerChangeConsumer implements IServerChangeConsumer {

    @Resource
    private ServerCenter serverCenter;

    @Override
    public String getKey() {
        return ServerChangeFlow.SERVER_FACTORY_UNACTIVE.getName();
    }

    @Override
    public BusinessWrapper<Boolean> consuming(OcServerChangeTask ocServerChangeTask, OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        OcServer ocServer = getServer(ocServerChangeTask);
        saveChangeTaskFlowStart(ocServerChangeTaskFlow); // 任务开始
        Boolean result = serverCenter.disable(ocServer);
        if (result) {
            saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow); // 任务结束
        } else {
            ChangeResult changeResult = ChangeResult.builder().build();
            saveChangeTaskFlowEnd(ocServerChangeTask,ocServerChangeTaskFlow, changeResult); // 任务结束
        }
        return BusinessWrapper.SUCCESS;
    }


}
