package com.baiyi.opscloud.factory.change.impl;

import com.baiyi.opscloud.common.base.ServerChangeFlow;
import com.baiyi.opscloud.common.base.ServerChangeType;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.factory.change.IServerChange;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/6/2 4:51 下午
 * @Version 1.0
 */
@Component
public class OnlineChange extends BaseServerChange implements IServerChange {

    @Override
    public String getKey() {
        return ServerChangeType.ONLINE.getType();
    }

    @Override
    public BusinessWrapper<Boolean> createFlow(OcServerChangeTask ocServerChangeTask, OcServer ocServer) {

        int parentId = addFlow(ocServerChangeTask, ServerChangeFlow.SERVER_ACTIVE.getName());

        parentId = addOcServerChangeTaskFlow(ocServerChangeTask, ServerChangeFlow.SERVER_POWER_ON.getName(), parentId).getId();

        parentId = addOcServerChangeTaskFlow(ocServerChangeTask, ServerChangeFlow.SERVER_TRY_SSH.getName(), parentId).getId();

        parentId = addOcServerChangeTaskFlow(ocServerChangeTask, ServerChangeFlow.SERVER_FACTORY_ACTIVE.getName(), parentId).getId();

        parentId = addOcServerChangeTaskFlow(ocServerChangeTask, ServerChangeFlow.SERVER_ONLINE.getName(), parentId).getId();

        addOcServerChangeTaskFlow(ocServerChangeTask, ServerChangeFlow.FINALIZED.getName(), parentId);

        return BusinessWrapper.SUCCESS;
    }
}
