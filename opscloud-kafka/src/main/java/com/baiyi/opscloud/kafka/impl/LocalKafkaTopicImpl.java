package com.baiyi.opscloud.kafka.impl;

import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.kafka.KafkaTopic;
import com.baiyi.opscloud.kafka.bo.KafkaBO;
import com.baiyi.opscloud.kafka.convert.KafkaTopicConvert;
import com.baiyi.opscloud.kafka.helper.KafkaClientHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/14 10:37 上午
 * @Since 1.0
 */

@Slf4j
@Component("LocalKafkaTopic")
public class LocalKafkaTopicImpl implements KafkaTopic {

    @Resource
    private KafkaClientHelper kafkaClientHelper;

    @Override
    public KafkaBO.Topic kafkaTopicQuery(KafkaParam.TopicQuery param) {
        AdminClient client = kafkaClientHelper.getKafkaAdminClient(param.getInstanceName());
        if (client == null)
            return null;
        DescribeTopicsResult result = client.describeTopics(Lists.newArrayList(param.getTopic()));
        try {
            TopicDescription description = result.all().get().get(param.getTopic());
            return KafkaTopicConvert.toBO(description);
        } catch (InterruptedException | ExecutionException e) {
            log.error("查询topic失败，topic:{}", param.getTopic(), e);
        }
        return null;
    }

    @Override
    public Boolean kafkaTopicCreate(KafkaParam.TopicCreate param) {
        AdminClient client = kafkaClientHelper.getKafkaAdminClient(param.getInstanceName());
        if (client == null)
            return false;
        NewTopic topic = new NewTopic(param.getTopic(), param.getPartitionNum(), (short) 1);
        CreateTopicsResult result = client.createTopics(Lists.newArrayList(topic));
        try {
            result.all().get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            log.error("创建topic失败，topic:{}", param.getTopic(), e);
        }
        return false;
    }

    @Override
    public Boolean kafkaTopicModify(KafkaParam.TopicModify param) {
        KafkaParam.TopicQuery queryParam = new KafkaParam.TopicQuery();
        queryParam.setTopic(param.getTopic());
        queryParam.setInstanceName(param.getInstanceName());
        KafkaBO.Topic topic = kafkaTopicQuery(queryParam);
        if (topic == null)
            return false;
        AdminClient client = kafkaClientHelper.getKafkaAdminClient(param.getInstanceName());
        if (client == null)
            return false;
        Map<String, NewPartitions> newPartitions = Maps.newHashMap();
        newPartitions.put(param.getTopic(), NewPartitions.increaseTo(topic.getNumPartitions() + param.getAddPartitionNum()));
        CreatePartitionsResult result = client.createPartitions(newPartitions);
        try {
            result.all().get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            log.error("修改topic失败，topic:{}", param.getTopic(), e);
        }
        return false;
    }
}
