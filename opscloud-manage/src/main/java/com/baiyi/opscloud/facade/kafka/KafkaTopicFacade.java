package com.baiyi.opscloud.facade.kafka;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.domain.vo.kafka.KafkaVO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/14 11:24 上午
 * @Since 1.0
 */
public interface KafkaTopicFacade {

    BusinessWrapper<KafkaVO.Topic> kafkaTopicQuery(KafkaParam.TopicQuery param);

    BusinessWrapper<Boolean> kafkaTopicCreate(KafkaParam.TopicCreate param);

    BusinessWrapper<Boolean> kafkaTopicModify(KafkaParam.TopicModify param);

    BusinessWrapper<Boolean> kafkaTopicCheck(String topic);
}
