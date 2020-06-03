package com.baiyi.opscloud.factory.change.impl;

import com.baiyi.opscloud.builder.ServerChangeTaskFlowBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.factory.change.IServerChange;
import com.baiyi.opscloud.factory.change.ServerChangeFactory;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskFlowService;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/27 4:14 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseServerChange implements IServerChange, InitializingBean {

    @Resource
    private OcServerChangeTaskService ocServerChangeTaskService;

    @Resource
    private OcServerChangeTaskFlowService ocServerChangeTaskFlowService;

    protected int addFlow(OcServerChangeTask ocServerChangeTask,String flowName){
        OcServerChangeTaskFlow serverUnactiveFlow = ServerChangeTaskFlowBuilder
                .build(ocServerChangeTask, flowName);
        addOcServerChangeTaskFlow(serverUnactiveFlow);

        ocServerChangeTask.setTaskFlowId(serverUnactiveFlow.getId());
        ocServerChangeTask.setTaskFlowName(flowName);
        updateOcServerChangeTask(ocServerChangeTask);
        return serverUnactiveFlow.getId();
    }

    protected void updateOcServerChangeTask(OcServerChangeTask ocServerChangeTask) {
        ocServerChangeTaskService.updateOcServerChangeTask(ocServerChangeTask);
    }

    protected OcServerChangeTaskFlow addOcServerChangeTaskFlow(OcServerChangeTask ocServerChangeTask, String flowName, int parentId) {
        OcServerChangeTaskFlow serverFactoryUnactiveFlow = ServerChangeTaskFlowBuilder
                .build(ocServerChangeTask, flowName, parentId);
        addOcServerChangeTaskFlow(serverFactoryUnactiveFlow);
        return serverFactoryUnactiveFlow;
    }

    protected void addOcServerChangeTaskFlow(OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        ocServerChangeTaskFlowService.addOcServerChangeTaskFlow(ocServerChangeTaskFlow);
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        ServerChangeFactory.register(this);
    }
}
