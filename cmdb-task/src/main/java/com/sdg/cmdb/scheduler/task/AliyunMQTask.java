package com.sdg.cmdb.scheduler.task;


import com.sdg.cmdb.service.AliyunMQService;
import com.sdg.cmdb.service.CacheKeyService;
import com.sdg.cmdb.util.schedule.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;

@Component
@Slf4j
public class AliyunMQTask implements BaseJob {

    public static final String ALIYUN_MQ_TASK_KEY = "AliyunMQTask";

    @Resource
    private AliyunMQService aliyunMQService;

    @Resource
    private CacheKeyService cacheKeyService;

    @Value("#{cmdb['task.open']}")
    private String taskOpen;

    @Override
    @Scheduled(cron = "0 0/5 * * * ?")
    public void execute() {
        if (!StringUtils.isEmpty(taskOpen) && taskOpen.equalsIgnoreCase("true")) {
            if (cacheKeyService.checkRunning(ALIYUN_MQ_TASK_KEY, 2))
                return;
            aliyunMQService.task();
            // 任务结束插入 结束
            cacheKeyService.set(ALIYUN_MQ_TASK_KEY, "false", 2);
        }

    }
}
