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
 * @Date 2020/5/27 4:20 下午
 * @Version 1.0
 */
@Component
public class OfflineChange extends BaseServerChange implements IServerChange {

    @Override
    public String getKey() {
        return ServerChangeType.OFFLINE.getType();
    }

    @Override
    public BusinessWrapper<Boolean> createFlow(OcServerChangeTask ocServerChangeTask, OcServer ocServer) {

        int parentId = addFlow(ocServerChangeTask, ServerChangeFlow.SERVER_UNACTIVE.getName());

        parentId = addOcServerChangeTaskFlow(ocServerChangeTask, ServerChangeFlow.SERVER_FACTORY_UNACTIVE.getName(), parentId).getId();

        parentId = addOcServerChangeTaskFlow(ocServerChangeTask, ServerChangeFlow.APPLICATION_STOP.getName(), parentId).getId();

        parentId = addOcServerChangeTaskFlow(ocServerChangeTask, ServerChangeFlow.SERVER_OFFLINE.getName(), parentId).getId();

        parentId = addOcServerChangeTaskFlow(ocServerChangeTask, ServerChangeFlow.SERVER_POWER_OFF.getName(), parentId).getId();

        addOcServerChangeTaskFlow(ocServerChangeTask, ServerChangeFlow.FINALIZED.getName(), parentId).getId();

        return BusinessWrapper.SUCCESS;
    }


}
