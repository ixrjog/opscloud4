package com.baiyi.opscloud.task;

import com.baiyi.opscloud.facade.aliyun.AliyunONSFacade;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/17 2:55 下午
 * @Since 1.0
 */
@Slf4j
@Component
public class AliyunONSTask extends BaseTask {
    
    @Resource
    private AliyunONSFacade aliyunONSFacade;

    public static final String TASK_ALIYUN_ONS_ALARM_KEY = "TASK_ALIYUN_ONS_ALARM_KEY";

    /**
     * 执行阿里云ONSGroupId告警任务
     */
    @Scheduled(cron = "0 */1 * * * ?")
    @SchedulerLock(name = "aliyunONSAlarmTask", lockAtMostFor = "2m", lockAtLeastFor = "2m")
    public void aliyunONSAlarmTask() {
        if (redisUtil.hasKey(TASK_ALIYUN_ONS_ALARM_KEY)) return;
        log.info("任务启动: 阿里云ONSGroupId告警任务！");
        aliyunONSFacade.onsGroupTask();
    }
}
