package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.bo.ServerChangeTaskBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;

/**
 * @Author baiyi
 * @Date 2020/5/27 4:45 下午
 * @Version 1.0
 */
public class ServerChangeTaskBuilder {

    public static OcServerChangeTask build(OcServer ocServer,String changeType,String taskId) {
        ServerChangeTaskBO bo = ServerChangeTaskBO.builder()
                .taskId(taskId)
                .serverId(ocServer.getId())
                .serverGroupId(ocServer.getServerGroupId())
                .changeType(changeType)
                .build();
        return covert(bo);
    }

    private static OcServerChangeTask covert(ServerChangeTaskBO bo) {
        return BeanCopierUtils.copyProperties(bo, OcServerChangeTask.class);
    }
}
