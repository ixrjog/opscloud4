package com.baiyi.opscloud.task;

import com.baiyi.opscloud.config.OpscloudConfig;
import com.baiyi.opscloud.facade.AttributeFacade;
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
public class AttributeTask {

    @Resource
    private OpscloudConfig opscloudConfig;

    @Resource
    private TaskUtil taskUtil;

    @Resource
    private AttributeFacade attributeFacade;

    public static final String TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY = "TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY";

    public static final String TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC = "TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC";

    /**
     * 执行ansible配置文件生成任务
     */
    @Scheduled(cron = "* */2 * * * ?")
    public void createAnsibleHostsConsumer() {
        if (!opscloudConfig.getOpenTask()) return;
        if (taskUtil.isTaskLock(TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY)) return;
        if (taskUtil.getSignalCount(TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC) == 0) return;
        log.info("任务: buildAnsibleHosts 开始执行!");
        attributeFacade.createAnsibleHosts();
        log.info("任务: buildAnsibleHosts 执行完成!");
    }


}
