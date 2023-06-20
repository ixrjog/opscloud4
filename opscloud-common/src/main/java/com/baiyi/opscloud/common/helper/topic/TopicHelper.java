package com.baiyi.opscloud.common.helper.topic;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/27 10:10 上午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TopicHelper {

    private static final long TOPIC_CACHE_MAX_TIME = NewTimeUtil.DAY_TIME / 1000;

    private static final String TOPIC_PREFIX = "TOPIC_";

    private final RedisUtil redisUtil;

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
        String topicName = buildTopic(topic);
        log.info("收到消息: topic={}", topicName);
        redisUtil.set(topicName, message, TOPIC_CACHE_MAX_TIME);
    }

    /**
     * 接收并删除消息
     *
     * @param topic
     * @return
     */
    public Object receive(String topic) {
        String topicName = buildTopic(topic);
        if (!redisUtil.hasKey(topicName)) {
            return null;
        }
        log.info("接收消息: topic={}", topicName);
        Object message = redisUtil.get(topicName);
        redisUtil.del(topicName);
        return message;
    }

    private String buildTopic(String topic) {
        return Joiner.on("").join(TOPIC_PREFIX, topic);
    }

}
