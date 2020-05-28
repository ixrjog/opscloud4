package com.baiyi.opscloud.factory.change.impl;

import com.baiyi.opscloud.builder.ServerChangeTaskFlowBuilder;
import com.baiyi.opscloud.common.base.ServerChangeFlow;
import com.baiyi.opscloud.common.base.ServerChangeType;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
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
        OcServerChangeTaskFlow serverUnactiveFlow =ServerChangeTaskFlowBuilder
                .build(ocServerChangeTask,ServerChangeFlow.SERVER_UNACTIVE.getName());
        addOcServerChangeTaskFlow(serverUnactiveFlow);








        return BusinessWrapper.SUCCESS;
    }
}
