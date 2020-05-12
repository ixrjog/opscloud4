package com.baiyi.opscloud.ansible.impl;

import com.baiyi.opscloud.ansible.IAnsibleExecutor;
import com.baiyi.opscloud.ansible.builder.ServerTaskBuilder;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnsibleScript;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.service.ansible.OcAnsibleScriptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/17 9:37 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AnsibleScriptExecutor extends BaseExecutor implements IAnsibleExecutor {

    public static final String COMPONENT_NAME = "AnsibleScriptExecutor";

    @Resource
    private OcAnsibleScriptService ocAnsibleScriptService;

    @Override
    public BusinessWrapper<Boolean> executorByParam(ServerTaskExecutorParam.TaskExecutor taskExecutor) {
        if (!(taskExecutor instanceof ServerTaskExecutorParam.ServerTaskScriptExecutor))
            return new BusinessWrapper(ErrorEnum.EXECUTOR_PARAM_TYPE_ERROR);
        ServerTaskExecutorParam.ServerTaskScriptExecutor serverTaskScriptExecutor = (ServerTaskExecutorParam.ServerTaskScriptExecutor) taskExecutor;
        OcUser ocUser = getOcUser();

        BusinessWrapper wrapper = getServerTreeHostPatternMap(serverTaskScriptExecutor.getUuid(), ocUser);
        if (!wrapper.isSuccess())
            return wrapper;
        Map<String, String> serverTreeHostPatternMap = (Map<String, String>) wrapper.getBody();

        // 录入任务
        OcServerTask ocServerTask = ServerTaskBuilder.build(ocUser, serverTreeHostPatternMap, serverTaskScriptExecutor);
        addOcServerTask(ocServerTask);
        // 重新写入脚本
        OcAnsibleScript ocAnsibleScript = ocAnsibleScriptService.queryOcAnsibleScriptById(serverTaskScriptExecutor.getScriptId());
        String scriptPath = ansibleConfig.getScriptPath(ocAnsibleScript);
        IOUtils.writeFile(ocAnsibleScript.getScriptContent(), scriptPath);

        // 异步执行
        ansibleTaskHandler.call(ocServerTask, serverTaskScriptExecutor, scriptPath);
        return getResultWrapper(ocServerTask);
    }

}
