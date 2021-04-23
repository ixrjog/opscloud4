package com.baiyi.opscloud.kafka.center;

import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.kafka.bo.KafkaBO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/15 2:45 下午
 * @Since 1.0
 */
public interface KafkaTopicCenter {

    KafkaBO.Topic kafkaTopicQuery(KafkaParam.TopicQuery param);

    Boolean kafkaTopicCreate(KafkaParam.TopicCreate param);

    Boolean kafkaTopicModify(KafkaParam.TopicModify param);
}
