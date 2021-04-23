package com.baiyi.opscloud.kafka.center.impl;

import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.kafka.KafkaGroup;
import com.baiyi.opscloud.kafka.bo.KafkaBO;
import com.baiyi.opscloud.kafka.center.KafkaGroupCenter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/18 11:17 上午
 * @Since 1.0
 */

@Component
public class KafkaGroupCenterImpl implements KafkaGroupCenter {

    @Resource(name = "AliyunKafkaGroup")
    private KafkaGroup aliyunKafkaGroup;

    @Override
    public KafkaBO.Group kafkaGroupQuery(KafkaParam.GroupQuery param) {
        List<KafkaBO.Group> groupList = aliyunKafkaGroup.kafkaGroupListQuery(param.getInstanceName());
        if (groupList.stream().anyMatch(x -> x.getConsumerId().equals(param.getConsumerId()))) {
            KafkaBO.Group group = new KafkaBO.Group();
            group.setConsumerId(param.getConsumerId());
            return group;
        }
        return null;
    }

    @Override
    public Boolean kafkaGroupCreate(KafkaParam.GroupCreate param) {
        return aliyunKafkaGroup.kafkaGroupCreate(param);
    }
}
