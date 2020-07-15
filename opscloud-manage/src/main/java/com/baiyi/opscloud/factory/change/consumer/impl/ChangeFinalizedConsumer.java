package com.baiyi.opscloud.factory.change.consumer.impl;

import com.baiyi.opscloud.common.base.ServerChangeFlow;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.facade.ServerCacheFacade;
import com.baiyi.opscloud.factory.change.consumer.IServerChangeConsumer;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource
    private ServerCacheFacade serverCacheFacade;

    @Resource
    private OcServerGroupService ocServerGroupService;


    @Override
    public BusinessWrapper<Boolean> consuming(OcServerChangeTask ocServerChangeTask, OcServerChangeTaskFlow
            ocServerChangeTaskFlow) {
        ocServerChangeTask.setResultCode(0);
        ocServerChangeTask.setResultMsg("SUCCESS");
        ocServerChangeTask.setEndTime(new Date());
        ocServerChangeTask.setTaskStatus(0);
        saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow);
        ocServerChangeTaskService.updateOcServerChangeTask(ocServerChangeTask);

        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(ocServerChangeTask.getServerGroupId());
        serverCacheFacade.evictServerGroupCache(ocServerGroup);
        return BusinessWrapper.SUCCESS;
    }


}
