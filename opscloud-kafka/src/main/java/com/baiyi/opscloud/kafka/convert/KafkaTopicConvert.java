package com.baiyi.opscloud.kafka.convert;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.kafka.bo.AliyunKafka;
import com.baiyi.opscloud.kafka.bo.KafkaBO;
import org.apache.kafka.clients.admin.TopicDescription;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/15 10:35 上午
 * @Since 1.0
 */
public class KafkaTopicConvert {

    public static KafkaBO.Topic toBO(TopicDescription topicDescription) {
        KafkaBO.Topic topic = new KafkaBO.Topic();
        topic.setTopic(topicDescription.name());
        topic.setNumPartitions(topicDescription.partitions().size());
        return topic;
    }

    public static KafkaBO.Topic toBO(String data) {
        AliyunKafka.TopicStatusResponse response = JSON.parseObject(data, AliyunKafka.TopicStatusResponse.class);
        if (response.getSuccess()) {
            AliyunKafka.TopicStatus topicStatus = response.getTopicStatus();
            KafkaBO.Topic topic = new KafkaBO.Topic();
            topic.setNumPartitions(topicStatus.getOffsetTable().getOffsetTable().size());
            topic.setTopic(topicStatus.getOffsetTable().getOffsetTable().get(0).getTopic());
            return topic;
        }
        return null;
    }
}
