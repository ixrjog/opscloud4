package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.facade.ServerCacheFacade;
import com.baiyi.opscloud.factory.attribute.impl.AttributeAnsible;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.task.util.TaskUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.baiyi.opscloud.task.AttributeTask.TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC;

/**
 * @Author baiyi
 * @Date 2020/4/10 10:19 上午
 * @Version 1.0
 */
@Component
public class ServerCacheFacadeImpl implements ServerCacheFacade {

    @Resource
    private AttributeAnsible attributeAnsible;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private TaskUtil taskUtil;

    @Override
    public void evictServerCache(OcServer ocServer) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(ocServer.getServerGroupId());
        evictServerGroupCache(ocServerGroup);
    }

    @Override
    public void evictServerGroupCache(OcServerGroup ocServerGroup) {
        attributeAnsible.evictGrouping(ocServerGroup);
        attributeAnsible.evictBuild(ocServerGroup);
        attributeAnsible.evictPreview(ocServerGroup.getId());
        // 通知任务执行
        taskUtil.sendMessage(TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC);
    }

}
