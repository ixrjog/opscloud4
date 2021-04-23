package com.baiyi.opscloud.kafka.center;

import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.kafka.bo.KafkaBO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/18 11:17 上午
 * @Since 1.0
 */
public interface KafkaGroupCenter {

    KafkaBO.Group kafkaGroupQuery(KafkaParam.GroupQuery param);

    Boolean kafkaGroupCreate(KafkaParam.GroupCreate param);
}
