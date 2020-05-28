package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.builder.ServerChangeTaskBuilder;
import com.baiyi.opscloud.common.base.ServerChangeType;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.param.server.ServerChangeParam;
import com.baiyi.opscloud.facade.ServerChangeFacade;
import com.baiyi.opscloud.factory.change.IServerChange;
import com.baiyi.opscloud.factory.change.ServerChangeFactory;
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

    /**
     * 服务器变更管理
     */

    @Override
    public BusinessWrapper<Boolean> executeServerChangeOffline(ServerChangeParam.ExecuteServerChangeParam executeServerChangeParam) {
        OcServerChangeTask query = new OcServerChangeTask();
        query.setServerId(executeServerChangeParam.getServerId());
        query.setTaskStatus(1);
        OcServerChangeTask check = ocServerChangeTaskService.checkOcServerChangeTask(query);
        if (check != null)
            return new BusinessWrapper<>(ErrorEnum.SERVER_CHANGE_TASK_RUNNING); // 任务运行中

        OcServer ocServer = ocServerService.queryOcServerById(executeServerChangeParam.getServerId());
        if (!ocServer.getServerGroupId().equals(executeServerChangeParam.getServerGroupId()))
            return new BusinessWrapper<>(ErrorEnum.SERVER_CHANGE_TASK_GROUP_ID_INCORRECT); // 组id不正确

        OcServerChangeTask ocServerChangeTask = ServerChangeTaskBuilder.build(ocServer, ServerChangeType.OFFLINE.getType());
        ocServerChangeTaskService.addOcServerChangeTask(ocServerChangeTask);

        IServerChange iServerChange = ServerChangeFactory.getServerChangeByKey(ServerChangeType.OFFLINE.getType());
        return iServerChange.createFlow(ocServerChangeTask, ocServer);
    }


}
