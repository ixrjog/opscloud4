package com.baiyi.opscloud.facade.task.builder;

import com.baiyi.opscloud.common.base.ServerTaskStatusEnum;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTaskMember;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;

/**
 * @Author baiyi
 * @Date 2021/9/22 9:46 上午
 * @Version 1.0
 */
public class ServerTaskMemberBuilder {

    public static ServerTaskMember newBuilder(ServerTask serverTask, ServerVO.Server server) {
        return ServerTaskMember.builder()
                .serverTaskId(serverTask.getId())
                .serverId(server.getId())
                .serverName(SimpleServerNameFacade.toServerName(server))
                .manageIp(server.getPrivateIp())
                .envType(server.getEnvType())
                .finalized(false)
                .taskStatus(ServerTaskStatusEnum.QUEUE.name())
                .stopType(0)
                .build();
    }

}