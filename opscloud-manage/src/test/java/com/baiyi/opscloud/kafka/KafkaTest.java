package com.baiyi.opscloud.kafka;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.domain.vo.kafka.KafkaVO;
import com.baiyi.opscloud.facade.kafka.KafkaTopicFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/13 4:11 下午
 * @Since 1.0
 */
public class KafkaTest extends BaseUnit {

    @Resource
    private KafkaTopicFacade kafkaTopicFacade;

    @Resource
    private KafkaGroup kafkaGroup;

    @Test
    void createTest() {
        KafkaParam.TopicCreate param = new KafkaParam.TopicCreate();
        param.setInstanceName("kafka-dev");
//        param.setInstanceName("kafka-canal-prod");
        param.setTopic("xiuyuan");
        param.setPartitionNum(6);
        param.setRemark("测试");
        kafkaTopicFacade.kafkaTopicCreate(param);
    }

    @Test
    void queryTest() {
        KafkaParam.TopicQuery param = new KafkaParam.TopicQuery();
        param.setInstanceName("kafka-dev");
//        param.setInstanceName("kafka-canal-prod");
        param.setTopic("xiuyuan");
        KafkaVO.Topic topic = kafkaTopicFacade.kafkaTopicQuery(param).getBody();
        System.err.println(topic);
    }

    @Test
    void modifyTest() {
        KafkaParam.TopicModify param = new KafkaParam.TopicModify();
        param.setInstanceName("kafka-dev");
//        param.setInstanceName("kafka-canal-prod");
        param.setTopic("xiuyuan");
        param.setAddPartitionNum(6);
        kafkaTopicFacade.kafkaTopicModify(param);
    }

    @Test
    void listTest() {
        kafkaGroup.kafkaGroupListQuery("kafka-dev");
    }
}
