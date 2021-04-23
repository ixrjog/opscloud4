package com.baiyi.opscloud.kafka.center.impl;

import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.kafka.KafkaTopic;
import com.baiyi.opscloud.kafka.bo.KafkaBO;
import com.baiyi.opscloud.kafka.center.KafkaTopicCenter;
import com.baiyi.opscloud.kafka.config.KafkaConfig;
import com.baiyi.opscloud.kafka.helper.KafkaClientHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/15 2:46 下午
 * @Since 1.0
 */

@Component("KafkaTopicCenter")
public class KafkaTopicCenterImpl implements KafkaTopicCenter {

    @Resource(name = "AliyunKafkaTopic")
    private KafkaTopic aliyunKafkaTopic;

    @Resource(name = "LocalKafkaTopic")
    private KafkaTopic localKafkaTopic;

    @Resource
    private KafkaClientHelper kafkaClientHelper;

    private KafkaTopic getKafkaTopic(String instanceName) {
        KafkaConfig.KafkaInstance instance = kafkaClientHelper.getKafkaInstance(instanceName);
        switch (instance.getInstanceType()) {
            case 1:
                return localKafkaTopic;
            case 2:
                return aliyunKafkaTopic;
            default:
                return null;
        }
    }

    @Override
    public KafkaBO.Topic kafkaTopicQuery(KafkaParam.TopicQuery param) {
        KafkaTopic kafkaTopic = getKafkaTopic(param.getInstanceName());
        if (kafkaTopic == null)
            return null;
        return kafkaTopic.kafkaTopicQuery(param);
    }

    @Override
    public Boolean kafkaTopicCreate(KafkaParam.TopicCreate param) {
        KafkaTopic kafkaTopic = getKafkaTopic(param.getInstanceName());
        if (kafkaTopic == null)
            return false;
        return kafkaTopic.kafkaTopicCreate(param);

    }

    @Override
    public Boolean kafkaTopicModify(KafkaParam.TopicModify param) {
        KafkaTopic kafkaTopic = getKafkaTopic(param.getInstanceName());
        if (kafkaTopic == null)
            return false;
        return kafkaTopic.kafkaTopicModify(param);

    }
}
