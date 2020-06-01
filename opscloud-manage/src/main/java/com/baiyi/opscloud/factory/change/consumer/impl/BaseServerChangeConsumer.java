package com.baiyi.opscloud.factory.change.consumer.impl;

import com.baiyi.opscloud.common.base.ServerChangeFlow;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.factory.change.consumer.IServerChangeConsumer;
import com.baiyi.opscloud.factory.change.consumer.ServerChangeConsumerFactory;
import com.baiyi.opscloud.factory.change.consumer.bo.ChangeResult;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskFlowService;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/5/29 9:33 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseServerChangeConsumer implements IServerChangeConsumer, InitializingBean {


    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcServerChangeTaskFlowService ocServerChangeTaskFlowService;

    @Resource
    protected OcServerChangeTaskService ocServerChangeTaskService;

    protected OcServer getServer(OcServerChangeTask ocServerChangeTask) {
        return ocServerService.queryOcServerById(ocServerChangeTask.getServerId());
    }

    protected void updateServer(OcServer ocServer) {
        ocServerService.updateOcServer(ocServer);
    }

    protected void updateOcServerChangeTaskFlow(OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        ocServerChangeTaskFlowService.updateOcServerChangeTaskFlow(ocServerChangeTaskFlow);
    }

    protected void saveChangeTaskFlowStart(OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        ocServerChangeTaskFlow.setTaskStatus(1);
        ocServerChangeTaskFlow.setStartTime(new Date());
        updateOcServerChangeTaskFlow(ocServerChangeTaskFlow);
    }

    protected void saveChangeTaskFlowEnd(OcServerChangeTask ocServerChangeTask, OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        ocServerChangeTaskFlow.setResultCode(0);
        ocServerChangeTaskFlow.setResultMsg("SUCCESS");
        ocServerChangeTaskFlow.setTaskStatus(0);
        if (ocServerChangeTaskFlow.getStartTime() == null)
            ocServerChangeTaskFlow.setStartTime(new Date());
        ocServerChangeTaskFlow.setEndTime(new Date());
        updateOcServerChangeTaskFlow(ocServerChangeTaskFlow);
        // next flow
        if (ocServerChangeTaskFlow.getTaskFlowName().equals(ServerChangeFlow.FINALIZED.getName()))
            return;
        OcServerChangeTaskFlow nexFlow = ocServerChangeTaskFlowService.queryOcServerChangeTaskFlowByParentId(ocServerChangeTaskFlow.getId());
        ocServerChangeTask.setTaskFlowName(nexFlow.getTaskFlowName());
        ocServerChangeTask.setTaskFlowId(nexFlow.getId());
        ocServerChangeTaskService.updateOcServerChangeTask(ocServerChangeTask);
    }

    protected void saveChangeTaskFlowEnd(OcServerChangeTask ocServerChangeTask,OcServerChangeTaskFlow ocServerChangeTaskFlow, ChangeResult changeResult) {
        ocServerChangeTaskFlow.setResultCode(changeResult.getCode());
        ocServerChangeTaskFlow.setResultMsg(changeResult.getMsg());
        ocServerChangeTaskFlow.setTaskStatus(0);
        if (ocServerChangeTaskFlow.getStartTime() == null)
            ocServerChangeTaskFlow.setStartTime(new Date());
        ocServerChangeTaskFlow.setEndTime(new Date());
        updateOcServerChangeTaskFlow(ocServerChangeTaskFlow);

        ocServerChangeTask.setTaskStatus(2); // 异常结束
        ocServerChangeTask.setResultCode(changeResult.getCode());
        ocServerChangeTask.setResultMsg(changeResult.getMsg());
        ocServerChangeTaskService.updateOcServerChangeTask(ocServerChangeTask);
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        ServerChangeConsumerFactory.register(this);
    }

}
