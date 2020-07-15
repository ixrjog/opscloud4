package com.baiyi.opscloud.task;

import com.baiyi.opscloud.facade.AttributeFacade;
import com.baiyi.opscloud.facade.ProfileSubscriptionFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/10 3:50 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class AttributeTask extends BaseTask {

    @Resource
    private AttributeFacade attributeFacade;

    @Resource
    private ProfileSubscriptionFacade profileSubscriptionFacade;

    public static final String TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY = "TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY";

    public static final String TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC = "TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC";

    /**
     * 执行ansible配置文件生成任务
     */
    @Scheduled(initialDelay = 10000, fixedRate = 60 * 1000)
    public void createAnsibleHostsConsumerTask() {
        if (taskUtil.getSignalCount(TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC) == 0) return;
        if (tryLock()) return;
        clearTopic();
        attributeFacade.createAnsibleHostsTask();
        profileSubscriptionFacade.publishProfile("ANSIBLE_HOSTS");
        unlock();
    }

    private void clearTopic() {
        taskUtil.clearSignalCount(TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC);
    }

    @Override
    protected String getLock() {
        return TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY;
    }

    @Override
    protected String getTaskName() {
        return "Ansible配置文件生成任务";
    }

}
