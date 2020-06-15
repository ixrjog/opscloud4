package com.baiyi.opscloud.task;

import com.baiyi.opscloud.facade.AliyunLogFacade;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Set;

import static com.baiyi.opscloud.common.base.Topic.TASK_ALIYUN_LOG_TOPIC;

/**
 * @Author baiyi
 * @Date 2020/6/15 4:48 下午
 * @Version 1.0
 */
public class AliyunLogTask extends BaseTask {
    @Resource
    private AliyunLogFacade aliyunLogFacade;

    public static final String TASK_ALIYUN_LOG_KEY = "TASK_ALIYUN_LOG_KEY";


    /**
     * 执行ansible配置文件生成任务
     */
    @Scheduled(initialDelay = 10000, fixedRate = 60 * 1000)
    public void createAnsibleHostsConsumer() {
        if (!redisUtil.hasKey(TASK_ALIYUN_LOG_TOPIC)) return;
        if (!tryLock(5)) return;
        Set<Integer> keySet = (Set<Integer>) redisUtil.get(TASK_ALIYUN_LOG_TOPIC);
        clearTopic();
        aliyunLogFacade.pushTask(keySet);
        unlock();
    }

    private void clearTopic(){
        redisUtil.del(TASK_ALIYUN_LOG_TOPIC);
    }

    @Override
    protected String getLock() {
        return TASK_ALIYUN_LOG_KEY;
    }

    @Override
    protected String getTaskName() {
        return "阿里云日志服务推送任务";
    }
}
