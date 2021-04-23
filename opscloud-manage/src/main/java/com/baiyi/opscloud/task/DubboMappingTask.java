package com.baiyi.opscloud.task;

import com.baiyi.opscloud.facade.NginxTcpServerFacade;
import com.baiyi.opscloud.facade.ProfileSubscriptionFacade;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/10/29 11:57 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class DubboMappingTask extends BaseTask {

    @Resource
    private NginxTcpServerFacade nginxTcpServerFacade;

    @Resource
    private ProfileSubscriptionFacade profileSubscriptionFacade;

    public static final String TASK_SERVER_DUBBO_MAPPING_KEY = "TASK_SERVER_DUBBO_MAPPING_KEY";

    public static final String TASK_SERVER_DUBBO_MAPPING_TOPIC = "TASK_SERVER_DUBBO_MAPPING_TOPIC";

    /**
     * 执行推送NginxTcp映射配置
     */
    @Scheduled(initialDelay = 10000, fixedRate = 60 * 1000)
    @SchedulerLock(name = "createNginxTcpMappingTask", lockAtMostFor = "5m", lockAtLeastFor = "5m")
    public void createNginxTcpMappingTask() {
        if (taskUtil.getSignalCount(TASK_SERVER_DUBBO_MAPPING_TOPIC) == 0) return;
        log.info("任务启动: 推送NginxTcp映射配置！");
        clearTopic();
        nginxTcpServerFacade.writeNginxDubboTcpServerConfByEnv("dev");
        nginxTcpServerFacade.writeNginxDubboTcpServerConfByEnv("daily");
        profileSubscriptionFacade.publishProfile("DUBBO_TCP_MAPPING");
    }

    private void clearTopic() {
        taskUtil.clearSignalCount(TASK_SERVER_DUBBO_MAPPING_TOPIC);
    }


}
