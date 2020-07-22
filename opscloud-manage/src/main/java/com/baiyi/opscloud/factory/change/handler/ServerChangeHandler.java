package com.baiyi.opscloud.factory.change.handler;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.factory.change.consumer.IServerChangeConsumer;
import com.baiyi.opscloud.factory.change.consumer.ServerChangeConsumerFactory;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskFlowService;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_EXECUTOR;

/**
 * @Author baiyi
 * @Date 2020/6/1 3:45 下午
 * @Version 1.0
 */
@Component
public class ServerChangeHandler {

    @Resource
    private OcServerChangeTaskFlowService ocServerChangeTaskFlowService;

    @Resource
    private OcServerChangeTaskService ocServerChangeTaskService;

    @Async(value = ASYNC_POOL_TASK_EXECUTOR)
    public void executeChangeTask(OcServerChangeTask ocServerChangeTask) {

        OcServerChangeTask taskRunning = ocServerChangeTask;
        while (true) {
            OcServerChangeTaskFlow ocServerChangeTaskFlow = ocServerChangeTaskFlowService.queryOcServerChangeTaskFlowById(taskRunning.getTaskFlowId());
            IServerChangeConsumer iServerChangeConsumer = ServerChangeConsumerFactory.getServerChangeConsumerByKey(taskRunning.getTaskFlowName());
            BusinessWrapper wrapper = iServerChangeConsumer.consuming(taskRunning, ocServerChangeTaskFlow);
            if (wrapper.isSuccess()) {
                taskRunning = ocServerChangeTaskService.queryOcServerChangeTaskByTaskId(taskRunning.getTaskId());
                if (taskRunning.getTaskStatus() != 1)
                    break;
            } else {
                break;
            }
        }
    }


}
