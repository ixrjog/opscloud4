package com.baiyi.opscloud.factory.change.consumer.impl;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.ansible.IAnsibleExecutor;
import com.baiyi.opscloud.ansible.factory.ExecutorFactory;
import com.baiyi.opscloud.ansible.impl.AnsibleScriptExecutor;
import com.baiyi.opscloud.common.base.ServerChangeFlow;
import com.baiyi.opscloud.common.base.ServerTaskType;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.server.ServerTaskVO;
import com.baiyi.opscloud.facade.ServerBaseFacade;
import com.baiyi.opscloud.facade.ServerTaskFacade;
import com.baiyi.opscloud.factory.change.consumer.IServerChangeConsumer;
import com.baiyi.opscloud.factory.change.consumer.bo.ChangeResult;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2020/5/29 5:40 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ApplicationStopConsumer extends BaseServerChangeConsumer implements IServerChangeConsumer {

    private static final int APPLICATION_STOP_SCRIPT_ID = 14;

    private static final long TASK_TIMEOUT = 2 * 60 * 1000L;

    @Resource
    private ServerTaskFacade serverTaskFacade;

    @Override
    public String getKey() {
        return ServerChangeFlow.APPLICATION_STOP.getName();
    }

    @Override
    public BusinessWrapper<Boolean> consuming(OcServerChangeTask ocServerChangeTask, OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        OcServer ocServer = getServer(ocServerChangeTask);
        saveChangeTaskFlowStart(ocServerChangeTaskFlow); // 任务开始

        ServerTaskExecutorParam.ServerTaskScriptExecutor taskExecutor = buildTaskExecutorParam(ocServer);
        // 执行Script
        IAnsibleExecutor iAnsibleExecutor = ExecutorFactory.getAnsibleExecutorByKey(AnsibleScriptExecutor.COMPONENT_NAME);
        BusinessWrapper wrapper = iAnsibleExecutor.executor(taskExecutor, ocServer);

        ocServerChangeTaskFlow.setExternalType("SCRIPT_TASK");
        if (!wrapper.isSuccess()) {
            ChangeResult changeResult = ChangeResult.builder().build();
            saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow, changeResult); // 任务结束
            return wrapper;
        }
        // 更新任务id
        OcServerTask ocServerTask = (OcServerTask) wrapper.getBody();
        ocServerChangeTaskFlow.setExternalId(ocServerTask.getId());
        updateOcServerChangeTaskFlow(ocServerChangeTaskFlow);

        long startTaskTime = new Date().getTime();
        boolean exit = false;
        while (!exit) {
            try {
                TimeUnit.SECONDS.sleep(2); // 2秒延迟
                if (TimeUtils.checkTimeout(startTaskTime, TASK_TIMEOUT)) {
                    ChangeResult changeResult = ChangeResult.builder()
                            .msg(Joiner.on("").join("TASK_TIMEOUT:", (TASK_TIMEOUT / 1000), "s"))
                            .build();
                    saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow, changeResult); // 任务结束
                    return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_TIMEOUT);
                }
                ServerTaskVO.ServerTask task = queryTask(ocServerChangeTaskFlow.getExternalId());
                if (task.getFinalized() == 1) {
                    ocServerChangeTaskFlow.setTaskDetail(JSON.toJSONString(task));
                    saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow);
                    exit = true;
                }
            } catch (Exception ignored) {
            }
        }
        return BusinessWrapper.SUCCESS;
    }

    private ServerTaskVO.ServerTask queryTask(int taskId) {
        return serverTaskFacade.queryServerTaskByTaskId(taskId);
    }

    private ServerTaskExecutorParam.ServerTaskScriptExecutor buildTaskExecutorParam(OcServer ocServer) {
        ServerTaskExecutorParam.ServerTaskScriptExecutor taskExecutor = new ServerTaskExecutorParam.ServerTaskScriptExecutor();
        taskExecutor.setScriptId(APPLICATION_STOP_SCRIPT_ID);
        taskExecutor.setTaskType(ServerTaskType.SCRIPT.getType());
        taskExecutor.setConcurrent(1);
        Set<String> hostPatterns = Sets.newHashSet();
        hostPatterns.add(ServerBaseFacade.acqServerName(ocServer));
        taskExecutor.setHostPatterns(hostPatterns);
        return taskExecutor;
    }

}
