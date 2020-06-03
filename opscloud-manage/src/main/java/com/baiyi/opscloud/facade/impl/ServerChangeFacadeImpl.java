package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.builder.ServerChangeTaskBuilder;
import com.baiyi.opscloud.common.base.ServerChangeType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.ServerChangeTaskDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.param.server.ServerChangeParam;
import com.baiyi.opscloud.domain.vo.serverChange.ServerChangeTaskVO;
import com.baiyi.opscloud.facade.ServerChangeFacade;
import com.baiyi.opscloud.factory.change.IServerChange;
import com.baiyi.opscloud.factory.change.ServerChangeFactory;
import com.baiyi.opscloud.factory.change.handler.ServerChangeHandler;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/26 4:43 下午
 * @Version 1.0
 */
@Service
public class ServerChangeFacadeImpl implements ServerChangeFacade {

    @Resource
    private OcServerChangeTaskService ocServerChangeTaskService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private ServerChangeHandler serverChangeHandler;

    @Resource
    private ServerChangeTaskDecorator serverChangeTaskDecorator;

    /**
     * 服务器变更管理
     */

    @Override
    public BusinessWrapper<Boolean> executeServerChangeOffline(ServerChangeParam.ExecuteServerChangeParam executeServerChangeParam) {
        OcServer ocServer = ocServerService.queryOcServerById(executeServerChangeParam.getServerId());
        BusinessWrapper wrapper = checkServerChange(executeServerChangeParam, ocServer);
        if (!wrapper.isSuccess()) return wrapper;

        OcServerChangeTask ocServerChangeTask = ServerChangeTaskBuilder.build(ocServer, ServerChangeType.OFFLINE.getType(),executeServerChangeParam.getTaskId());
        ocServerChangeTaskService.addOcServerChangeTask(ocServerChangeTask);

        IServerChange iServerChange = ServerChangeFactory.getServerChangeByKey(ServerChangeType.OFFLINE.getType());
        iServerChange.createFlow(ocServerChangeTask, ocServer);
        serverChangeHandler.executeChangeTask(ocServerChangeTask);
        wrapper.setBody(ocServerChangeTask.getTaskId());
        return wrapper;
    }

    @Override
    public BusinessWrapper<Boolean> executeServerChangeOnline(ServerChangeParam.ExecuteServerChangeParam executeServerChangeParam) {
        OcServer ocServer = ocServerService.queryOcServerById(executeServerChangeParam.getServerId());
        BusinessWrapper wrapper = checkServerChange(executeServerChangeParam, ocServer);
        if (!wrapper.isSuccess()) return wrapper;

        OcServerChangeTask ocServerChangeTask = ServerChangeTaskBuilder.build(ocServer, ServerChangeType.ONLINE.getType(),executeServerChangeParam.getTaskId());
        ocServerChangeTaskService.addOcServerChangeTask(ocServerChangeTask);

        IServerChange iServerChange = ServerChangeFactory.getServerChangeByKey(ServerChangeType.ONLINE.getType());
        iServerChange.createFlow(ocServerChangeTask, ocServer);
        serverChangeHandler.executeChangeTask(ocServerChangeTask);

        wrapper.setBody(ocServerChangeTask.getTaskId());
        return wrapper;
    }

    private BusinessWrapper<Boolean> checkServerChange(ServerChangeParam.ExecuteServerChangeParam executeServerChangeParam, OcServer ocServer) {
        OcServerChangeTask query = new OcServerChangeTask();
        query.setServerId(executeServerChangeParam.getServerId());
        query.setTaskStatus(1);
        OcServerChangeTask checkTaskRunning = ocServerChangeTaskService.checkOcServerChangeTask(query);
        if (checkTaskRunning != null)
            return new BusinessWrapper<>(ErrorEnum.SERVER_CHANGE_TASK_RUNNING); // 任务运行中

        OcServerChangeTask changeTaskId  = ocServerChangeTaskService.queryOcServerChangeTaskByTaskId(executeServerChangeParam.getTaskId());
        if (changeTaskId != null)
            return new BusinessWrapper<>(ErrorEnum.SERVER_CHANGE_TASK_RESUBMISSION); // 任务重复提交

        if (!ocServer.getServerGroupId().equals(executeServerChangeParam.getServerGroupId()))
            return new BusinessWrapper<>(ErrorEnum.SERVER_CHANGE_TASK_GROUP_ID_INCORRECT); // 组id不正确
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public ServerChangeTaskVO.ServerChangeTask queryServerChangeTask(String taskId) {
        OcServerChangeTask task = ocServerChangeTaskService.queryOcServerChangeTaskByTaskId(taskId);
        ServerChangeTaskVO.ServerChangeTask serverChangeTask = BeanCopierUtils.copyProperties(task, ServerChangeTaskVO.ServerChangeTask.class);
        return serverChangeTaskDecorator.decorator(serverChangeTask);
    }


}
