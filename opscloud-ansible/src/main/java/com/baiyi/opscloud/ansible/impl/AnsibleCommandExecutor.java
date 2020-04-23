package com.baiyi.opscloud.ansible.impl;

import com.baiyi.opscloud.ansible.IAnsibleExecutor;
import com.baiyi.opscloud.ansible.builder.ServerTaskBuilder;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/17 9:04 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AnsibleCommandExecutor extends BaseExecutor implements IAnsibleExecutor {

    public static final String COMPONENT_NAME = "AnsibleCommandExecutor";

    @Override
    public BusinessWrapper<Boolean> executorByParam(ServerTaskExecutorParam.TaskExecutor taskExecutor) {
        if (!(taskExecutor instanceof ServerTaskExecutorParam.ServerTaskCommandExecutor))
            return new BusinessWrapper(ErrorEnum.EXECUTOR_PARAM_TYPE_ERROR);
        ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskCommandExecutor = (ServerTaskExecutorParam.ServerTaskCommandExecutor) taskExecutor;
        OcUser ocUser = getOcUser();

        BusinessWrapper wrapper = getServerTreeHostPatternMap(serverTaskCommandExecutor.getUuid(), ocUser);
        if (!wrapper.isSuccess())
            return wrapper;
        Map<String, String> serverTreeHostPatternMap = (Map<String, String>) wrapper.getBody();

        // 录入任务
        OcServerTask ocServerTask = ServerTaskBuilder.build(ocUser, serverTreeHostPatternMap, serverTaskCommandExecutor);
        addOcServerTask(ocServerTask);
        // 异步执行
        ansibleTaskHandler.call(ocServerTask, serverTaskCommandExecutor);
        return getResultWrapper(ocServerTask);
    }


}
