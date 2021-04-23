package com.baiyi.opscloud.kafka.convert;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.kafka.bo.AliyunKafka;
import com.baiyi.opscloud.kafka.bo.KafkaBO;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/19 10:48 上午
 * @Since 1.0
 */
public class KafkaGroupConvert {

    public static KafkaBO.Group toBO(String data) {
        AliyunKafka.ConsumerProgressResponse response = JSON.parseObject(data, AliyunKafka.ConsumerProgressResponse.class);
        if (response.getSuccess()) {
            AliyunKafka.ConsumerProgress consumerProgress = response.getConsumerProgress();
            KafkaBO.Group group = new KafkaBO.Group();
            return group;
        }
        return null;
    }

    public static List<KafkaBO.Group> toBOList(String data) {
        AliyunKafka.GroupListResponse response = JSON.parseObject(data, AliyunKafka.GroupListResponse.class);
        if (response.getSuccess()) {
            List<KafkaBO.Group> groupList = Lists.newArrayList();
            AliyunKafka.ConsumerList consumerList = response.getConsumerList();
            consumerList.getConsumerVO().stream().forEach(ConsumerVO -> {
                KafkaBO.Group group = new KafkaBO.Group();
                group.setConsumerId(ConsumerVO.getConsumerId());
                groupList.add(group);
            });
            return groupList;
        }
        return Collections.emptyList();
    }
}
