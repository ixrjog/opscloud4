package com.baiyi.opscloud.kafka;

import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.kafka.bo.KafkaBO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/18 11:11 上午
 * @Since 1.0
 */
public interface KafkaGroup {

    KafkaBO.Group kafkaGroupQuery(KafkaParam.GroupQuery param);

    Boolean kafkaGroupCreate(KafkaParam.GroupCreate param);

    List<KafkaBO.Group> kafkaGroupListQuery(String instanceName);
}
