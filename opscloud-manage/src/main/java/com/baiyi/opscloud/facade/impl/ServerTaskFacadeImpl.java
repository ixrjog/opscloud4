package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.ansible.handler.AnsibleTaskHandler;
import com.baiyi.opscloud.builder.ServerTaskBuilder;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.RedisKeyUtils;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.decorator.ServerTaskDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.server.OcServerTaskVO;
import com.baiyi.opscloud.facade.ServerTaskFacade;
import com.baiyi.opscloud.service.server.OcServerTaskService;
import com.baiyi.opscloud.service.user.OcUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:36 下午
 * @Version 1.0
 */
@Service
public class ServerTaskFacadeImpl implements ServerTaskFacade {

    @Resource
    private AnsibleTaskHandler ansibleTaskHandler;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcServerTaskService ocServerTaskService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ServerTaskDecorator serverTaskDecorator;

    @Override
    public BusinessWrapper<Boolean> executorCommand(ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskCommandExecutor) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(SessionUtils.getUsername());
        String key = RedisKeyUtils.getMyServerTreeKey(ocUser.getId(), serverTaskCommandExecutor.getUuid());
        if (!redisUtil.hasKey(key))
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_TREE_NOT_EXIST);
        Map<String, String> serverTreeHostPatternMap = (Map<String, String>) redisUtil.get(key);
        // 录入任务
        OcServerTask ocServerTask = ServerTaskBuilder.build(ocUser, serverTreeHostPatternMap);
        ocServerTaskService.addOcServerTask(ocServerTask);
        // 异步执行
        ansibleTaskHandler.call(ocServerTask, serverTaskCommandExecutor);
        BusinessWrapper wrapper = BusinessWrapper.SUCCESS;
        wrapper.setBody(ocServerTask);
        return wrapper;
    }

    @Override
    public OcServerTaskVO.ServerTask queryServerTaskByTaskId(int taskId) {
        OcServerTask ocServerTask = ocServerTaskService.queryOcServerTaskById(taskId);
        OcServerTaskVO.ServerTask serverTask = BeanCopierUtils.copyProperties(ocServerTask, OcServerTaskVO.ServerTask.class);
        return serverTaskDecorator.decorator(serverTask);
    }

}
