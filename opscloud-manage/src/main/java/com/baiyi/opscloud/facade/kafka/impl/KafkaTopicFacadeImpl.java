package com.baiyi.opscloud.facade.kafka.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.domain.vo.kafka.KafkaVO;
import com.baiyi.opscloud.facade.kafka.KafkaTopicFacade;
import com.baiyi.opscloud.kafka.bo.KafkaBO;
import com.baiyi.opscloud.kafka.center.KafkaTopicCenter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/14 11:24 上午
 * @Since 1.0
 */

@Component
public class KafkaTopicFacadeImpl implements KafkaTopicFacade {

    @Resource
    private KafkaTopicCenter kafkaTopicCenter;

    @Override
    public BusinessWrapper<KafkaVO.Topic> kafkaTopicQuery(KafkaParam.TopicQuery param) {
        KafkaBO.Topic topic = kafkaTopicCenter.kafkaTopicQuery(param);
        if (topic == null)
            return new BusinessWrapper<>(ErrorEnum.KAFKA_TOPIC_NOT_EXIST);
        return new BusinessWrapper<>(BeanCopierUtils.copyProperties(topic, KafkaVO.Topic.class));
    }

    @Override
    public BusinessWrapper<Boolean> kafkaTopicCreate(KafkaParam.TopicCreate param) {
        KafkaParam.TopicQuery queryParam = new KafkaParam.TopicQuery();
        queryParam.setInstanceName(param.getInstanceName());
        queryParam.setTopic(param.getTopic());
        KafkaBO.Topic topic = kafkaTopicCenter.kafkaTopicQuery(queryParam);
        if (topic != null)
            return new BusinessWrapper<>(ErrorEnum.KAFKA_TOPIC_EXIST);
        if (kafkaTopicCenter.kafkaTopicCreate(param)) {
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.KAFKA_TOPIC_CREATE_FAIL);
    }

    @Override
    public BusinessWrapper<Boolean> kafkaTopicModify(KafkaParam.TopicModify param) {
        KafkaParam.TopicQuery queryParam = new KafkaParam.TopicQuery();
        queryParam.setInstanceName(param.getInstanceName());
        queryParam.setTopic(param.getTopic());
        KafkaBO.Topic topic = kafkaTopicCenter.kafkaTopicQuery(queryParam);
        if (topic == null)
            return new BusinessWrapper<>(ErrorEnum.KAFKA_TOPIC_NOT_EXIST);
        if (kafkaTopicCenter.kafkaTopicModify(param)) {
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.KAFKA_TOPIC_CREATE_FAIL);
    }

    @Override
    public BusinessWrapper<Boolean> kafkaTopicCheck(String topic) {
        if (!RegexUtils.isKafkaTopic(topic))
            return new BusinessWrapper<>(ErrorEnum.KAFKA_TOPIC_ERR);
        return BusinessWrapper.SUCCESS;
    }
}
