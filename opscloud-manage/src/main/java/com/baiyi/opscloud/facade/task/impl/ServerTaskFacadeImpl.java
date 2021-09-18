package com.baiyi.opscloud.facade.task.impl;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTaskMember;
import com.baiyi.opscloud.domain.param.task.ServerTaskParam;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.domain.vo.task.ServerTaskVO;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.facade.task.ServerTaskFacade;
import com.baiyi.opscloud.service.task.ServerTaskMemberService;
import com.baiyi.opscloud.service.task.ServerTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/18 3:21 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ServerTaskFacadeImpl implements ServerTaskFacade {

    @Resource
    private ServerTaskService serverTaskService;

    @Resource
    private ServerTaskMemberService serverTaskMemberService;

    public ServerTaskVO.ServerTask submitServerTask(ServerTaskParam.SubmitServerTask submitServerTask) {
        ServerTask serverTask = ServerTask.builder()
                .taskUuid(IdUtil.buildUUID())
                .ansiblePlaybookId(submitServerTask.getAnsiblePlaybookId())
                .username(SessionUtil.getUsername())
                .memberSize(submitServerTask.getServers().size())
                .taskType(submitServerTask.getTaskType())
                .vars(submitServerTask.getVars())
                .tags(submitServerTask.getTags())
                .stopType(0)
                .finalized(false)
                .startTime(new Date())
                .build();
        serverTaskService.add(serverTask);
        record(serverTask, submitServerTask.getServers());
        return ServerTaskVO.ServerTask.builder().build();
    }

    private void record(ServerTask serverTask, List<ServerVO.Server> servers) {
        servers.forEach(s -> {
            ServerTaskMember member = ServerTaskMember.builder()
                    .serverTaskId(serverTask.getId())
                    .serverId(s.getId())
                    .serverName(SimpleServerNameFacade.toServerName(s))
                    .manageIp(s.getPrivateIp())
                    .envType(s.getEnvType())
                    .finalized(false)
                    .stopType(0)
                    .build();
            serverTaskMemberService.add(member);
        });
    }


}
