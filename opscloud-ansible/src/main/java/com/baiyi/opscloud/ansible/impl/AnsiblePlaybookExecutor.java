package com.baiyi.opscloud.ansible.impl;

import com.baiyi.opscloud.ansible.IAnsibleExecutor;
import com.baiyi.opscloud.ansible.builder.ServerTaskBuilder;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnsiblePlaybook;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.service.ansible.OcAnsiblePlaybookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/17 9:54 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AnsiblePlaybookExecutor extends BaseExecutor implements IAnsibleExecutor {

    public static final String COMPONENT_NAME = "AnsiblePlaybookExecutor";

    @Resource
    private OcAnsiblePlaybookService ocAnsiblePlaybookService;

    @Override
    public BusinessWrapper<Boolean> executorByParam(ServerTaskExecutorParam.TaskExecutor taskExecutor) {
        if (!(taskExecutor instanceof ServerTaskExecutorParam.ServerTaskPlaybookExecutor))
            return new BusinessWrapper(ErrorEnum.EXECUTOR_PARAM_TYPE_ERROR);
        ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor = (ServerTaskExecutorParam.ServerTaskPlaybookExecutor) taskExecutor;
        OcUser ocUser = getOcUser();

        BusinessWrapper wrapper = getServerTreeHostPatternMap(serverTaskPlaybookExecutor.getUuid(), ocUser);
        if (!wrapper.isSuccess())
            return wrapper;
        Map<String, String> serverTreeHostPatternMap = (Map<String, String>) wrapper.getBody();

        // 录入任务
        OcServerTask ocServerTask = ServerTaskBuilder.build(ocUser, serverTreeHostPatternMap, serverTaskPlaybookExecutor);
        addOcServerTask(ocServerTask);
        // 重新写入playbook
        OcAnsiblePlaybook ocAnsiblePlaybook = ocAnsiblePlaybookService.queryOcAnsiblePlaybookById(serverTaskPlaybookExecutor.getPlaybookId());
        String playbookPath = ansibleConfig.getPlaybookPath(ocAnsiblePlaybook);
        IOUtils.writeFile(ocAnsiblePlaybook.getPlaybook(), playbookPath);

        // 异步执行
        ansibleTaskHandler.call(ocServerTask, serverTaskPlaybookExecutor, playbookPath);
        return getResultWrapper(ocServerTask);
    }

}
