package com.baiyi.opscloud.factory.change.consumer.impl;

import com.baiyi.opscloud.common.base.ServerChangeFlow;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.factory.change.consumer.IServerChangeConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/5/29 11:33 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class ServerUnactiveConsumer extends BaseServerChangeConsumer implements IServerChangeConsumer {

    @Override
    public String getKey() {
        return ServerChangeFlow.SERVER_UNACTIVE.getName();
    }


    @Override
    public BusinessWrapper<Boolean> consuming(OcServerChangeTask ocServerChangeTask, OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        OcServer ocServer = getServer(ocServerChangeTask);
        ocServer.setIsActive(false);
        updateServer(ocServer);

        saveChangeTaskFlowEnd(ocServerChangeTask,ocServerChangeTaskFlow);

        return BusinessWrapper.SUCCESS;
    }


}
