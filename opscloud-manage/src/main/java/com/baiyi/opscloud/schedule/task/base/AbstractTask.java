package com.baiyi.opscloud.schedule.task.base;


import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.helper.TopicHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;


/**
 * @Author baiyi
 * @Date 2020/6/8 1:37 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractTask {

    @Value("${spring.profiles.active}")
    protected String env;

    @Resource
    protected RedisUtil redisUtil;

    @Resource
    private TopicHelper topicHelper;

    protected Object receive(String topic) {
        return topicHelper.receive(topic);
    }

}
