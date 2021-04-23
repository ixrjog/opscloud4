package com.baiyi.opscloud.task;

import com.baiyi.opscloud.facade.AttributeFacade;
import com.baiyi.opscloud.facade.ProfileSubscriptionFacade;
import com.baiyi.opscloud.facade.prometheus.PrometheusFacade;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
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
    private PrometheusFacade prometheusFacade;

    @Resource
    private ProfileSubscriptionFacade profileSubscriptionFacade;

    public interface profiles {
        String PROMETHEUS_CONFIG = "PROMETHEUS_CONFIG";
        String ANSIBLE_HOSTS = "ANSIBLE_HOSTS";
        String JUMPSERVER = "JUMPSERVER";
    }

    public static final String TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY = "TASK_SERVER_ATTRIBUTE_ANSIBLE_HOSTS_KEY";

    public static final String TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC = "TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC";

    /**
     * 执行ansible配置文件生成任务
     */
    @Scheduled(initialDelay = 10000, fixedRate = 60 * 1000)
    @SchedulerLock(name = "createAnsibleHostsConsumerTask", lockAtMostFor = "2m", lockAtLeastFor = "2m")
    public void createAnsibleHostsConsumerTask() {
        if (taskUtil.getSignalCount(TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC) == 0) return;
        log.info("任务启动: Ansible配置文件生成任务！");
        clearTopic();
        createConfigTask();
        profileSubscriptionFacade.publishProfile(profiles.PROMETHEUS_CONFIG);
        profileSubscriptionFacade.publishProfile(profiles.ANSIBLE_HOSTS);
        profileSubscriptionFacade.publishProfile(profiles.JUMPSERVER);

    }

    private void createConfigTask() {
        attributeFacade.createAnsibleHostsTask();
        prometheusFacade.createPrometheusConfigTask();
    }

    private void clearTopic() {
        taskUtil.clearSignalCount(TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC);
    }


}
