package com.baiyi.opscloud.ansible.builder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.ansible.bo.ServerTaskBO;
import com.baiyi.opscloud.common.base.ServerTaskType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/7 9:13 下午
 * @Version 1.0
 */
public class ServerTaskBuilder {

    public static OcServerTask build(OcUser ocUser, Map<String, String> serverTreeHostPatternMap, ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskExecutor) {
        return build(ocUser, serverTreeHostPatternMap, JSON.toJSONString(serverTaskExecutor), ServerTaskType.COMMAND.getType());
    }

    public static OcServerTask build(OcUser ocUser, Map<String, String> serverTreeHostPatternMap, ServerTaskExecutorParam.ServerTaskScriptExecutor serverTaskExecutor) {
        return build(ocUser, serverTreeHostPatternMap, JSON.toJSONString(serverTaskExecutor),  ServerTaskType.SCRIPT.getType());
    }

    public static OcServerTask build(Map<String, String> serverTreeHostPatternMap, ServerTaskExecutorParam.ServerTaskScriptExecutor serverTaskExecutor) {
        return build( serverTreeHostPatternMap, JSON.toJSONString(serverTaskExecutor), ServerTaskType.SCRIPT.getType());
    }

    public static OcServerTask build(OcUser ocUser, Map<String, String> serverTreeHostPatternMap, ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskExecutor) {
        return build(ocUser, serverTreeHostPatternMap, JSON.toJSONString(serverTaskExecutor), ServerTaskType.PLAYBOOK.getType());
    }

    public static OcServerTask build( Map<String, String> serverTreeHostPatternMap, String paramJson, int taskType) {
        ServerTaskBO serverTaskBO = ServerTaskBO.builder()
                .userId(0)
                .taskType(taskType)
                .executorParam(paramJson)
                .systemType(1)
                .serverTargetDetail(JSON.toJSONString(serverTreeHostPatternMap))
                .build();
        return covert(serverTaskBO);
    }


    public static OcServerTask build(OcUser ocUser, Map<String, String> serverTreeHostPatternMap, String paramJson, int taskType) {
        ServerTaskBO serverTaskBO = ServerTaskBO.builder()
                .userId(ocUser != null ? ocUser.getId() : 0)
                .taskType(taskType)
                .userDetail(JSON.toJSONString(ocUser))
                .executorParam(paramJson)
                .serverTargetDetail(JSON.toJSONString(serverTreeHostPatternMap))
                .build();

        return covert(serverTaskBO);
    }

    private static OcServerTask covert(ServerTaskBO serverTaskBO) {
        return BeanCopierUtils.copyProperties(serverTaskBO, OcServerTask.class);
    }
}
