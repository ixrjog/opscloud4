package com.baiyi.opscloud.decorator.server;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.domain.vo.serverChange.ServerChangeTaskVO;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/3 10:05 上午
 * @Version 1.0
 */
@Slf4j
@Component("ServerChangeTaskDecorator")
public class ServerChangeTaskDecorator {

    @Resource
    private OcServerChangeTaskFlowService ocServerChangeTaskFlowService;

    public ServerChangeTaskVO.ServerChangeTask decorator(ServerChangeTaskVO.ServerChangeTask serverChangeTask) {
        if(serverChangeTask == null ) return serverChangeTask;
        if(StringUtils.isEmpty(serverChangeTask.getTaskId())) return serverChangeTask;
        log.info("ServerChangeTaskDecorator taskId= {}",serverChangeTask.getTaskId());
        List<OcServerChangeTaskFlow> taskFlowList = ocServerChangeTaskFlowService.queryOcServerChangeTaskFlowByTaskId(serverChangeTask.getTaskId());
        if (taskFlowList != null)
            serverChangeTask.setTaskFlows(BeanCopierUtils.copyListProperties(taskFlowList, ServerChangeTaskVO.ServerChangeTaskFlow.class));

        return serverChangeTask;
    }
}
