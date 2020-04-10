package com.baiyi.opscloud.task;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.facade.AttributeFacade;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/10 3:50 下午
 * @Version 1.0
 */
@Component
public class AttributeTask {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private TaskUtil taskUtil;

    @Resource
    private AttributeFacade attributeFacade;

    public static final String TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY = "TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY";

    /**
     * 每2分组执行一次，ansible配置文件生成任务
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void cronBuildAnsibleHosts() {
        // 任务执行中
        if (taskUtil.isTaskLock(TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY)) return;
        taskUtil.setTaskLock(TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY, 5);
        attributeFacade.createAnsibleHosts();
        taskUtil.delTaskLock(TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY);
    }


}
