package com.baiyi.opscloud.common.topic;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/27 10:10 上午
 * @Version 1.0
 */
@Component
public class TopicHelper {

    private static final long TOPIC_CACHE_MAX_TIME = TimeUtil.dayTime / 1000;

    private static final String TOPIC_PREFIX = "TOPIC_";

    @Resource
    private RedisUtil redisUtil;

    public interface Topics {
        String ASSET_SUBSCRIPTION_TASK = "ASSET_SUBSCRIPTION_TASK";
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param message
     */
    public void send(String topic, Object message) {
        redisUtil.set(Joiner.on("").join(TOPIC_PREFIX, topic), message, TOPIC_CACHE_MAX_TIME);
    }

    /**
     * 接收并删除消息
     *
     * @param topic
     * @return
     */
    public Object receive(String topic) {
        if (!redisUtil.hasKey(topic)) return null;
        Object message = redisUtil.get(topic);
        redisUtil.del(Joiner.on("").join(TOPIC_PREFIX, topic));
        return message;
    }


}
