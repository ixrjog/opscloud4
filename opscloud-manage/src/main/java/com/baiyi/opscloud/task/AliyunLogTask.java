package com.baiyi.opscloud.task;

import com.baiyi.opscloud.facade.aliyun.AliyunLogFacade;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

import static com.baiyi.opscloud.common.base.Topic.TASK_ALIYUN_LOG_TOPIC;

/**
 * @Author baiyi
 * @Date 2020/6/15 4:48 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunLogTask extends BaseTask {

    @Resource
    private AliyunLogFacade aliyunLogFacade;

    public static final String TASK_ALIYUN_LOG_KEY = "TASK_ALIYUN_LOG_KEY";

    /**
     * 执行阿里云日志服务推送任务
     */
    @Scheduled(initialDelay = 10000, fixedRate = 60 * 1000)
    @SchedulerLock(name = "aliyunLogPushTask", lockAtMostFor = "2m", lockAtLeastFor = "2m")
    public void aliyunLogPushTask() {
        if (redisUtil.hasKey(TASK_ALIYUN_LOG_TOPIC)) return;
        log.info("任务启动: 阿里云日志服务推送任务！");
        Set<Integer> keySet = (Set<Integer>) redisUtil.get(TASK_ALIYUN_LOG_TOPIC);
        clearTopic();
        aliyunLogFacade.pushTask(keySet);
    }

    private void clearTopic(){
        redisUtil.del(TASK_ALIYUN_LOG_TOPIC);
    }

}
