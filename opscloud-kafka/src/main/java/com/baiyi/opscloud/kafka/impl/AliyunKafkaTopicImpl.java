package com.baiyi.opscloud.kafka.impl;

import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.kafka.KafkaTopic;
import com.baiyi.opscloud.kafka.bo.KafkaBO;
import com.baiyi.opscloud.kafka.convert.KafkaTopicConvert;
import com.baiyi.opscloud.kafka.handler.AliyunKafkaTopicHandler;
import org.apache.logging.log4j.util.Strings;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/14 5:35 下午
 * @Since 1.0
 */

@Component("AliyunKafkaTopic")
public class AliyunKafkaTopicImpl implements KafkaTopic {

    @Resource
    private AliyunKafkaTopicHandler aliyunKafkaTopicHandler;

    @Override
    public KafkaBO.Topic kafkaTopicQuery(KafkaParam.TopicQuery param) {
        String topic = aliyunKafkaTopicHandler.kafkaTopicQuery(param);
        return Strings.isEmpty(topic) ? null : KafkaTopicConvert.toBO(topic);
    }

    @Override
    @Retryable(value = RuntimeException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public Boolean kafkaTopicCreate(KafkaParam.TopicCreate param) throws RuntimeException {
        Boolean result = aliyunKafkaTopicHandler.kafkaTopicCreate(param);
        if (!result) {
            throw new RuntimeException("创建kafka topic失败,retry");
        }
        return true;
    }

    @Override
    public Boolean kafkaTopicModify(KafkaParam.TopicModify param) {
        return aliyunKafkaTopicHandler.kafkaTopicModify(param);
    }
}
