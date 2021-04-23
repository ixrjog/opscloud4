package com.baiyi.opscloud.kafka.impl;

import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.kafka.KafkaGroup;
import com.baiyi.opscloud.kafka.bo.KafkaBO;
import com.baiyi.opscloud.kafka.convert.KafkaGroupConvert;
import com.baiyi.opscloud.kafka.handler.AliyunKafkaGroupHandler;
import org.apache.logging.log4j.util.Strings;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/18 11:12 上午
 * @Since 1.0
 */

@Component("AliyunKafkaGroup")
public class AliyunKafkaGroupImpl implements KafkaGroup {

    @Resource
    private AliyunKafkaGroupHandler aliyunKafkaGroupHandler;

    @Override
    public KafkaBO.Group kafkaGroupQuery(KafkaParam.GroupQuery param) {
        String groupResponse = aliyunKafkaGroupHandler.kafkaGroupQuery(param);
        if (Strings.isEmpty(groupResponse))
            return null;
        KafkaBO.Group group = KafkaGroupConvert.toBO(groupResponse);
        if (group != null)
            group.setConsumerId(param.getConsumerId());
        return group;
    }

    @Override
    @Retryable(value = RuntimeException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public Boolean kafkaGroupCreate(KafkaParam.GroupCreate param) throws RuntimeException {
        Boolean result = aliyunKafkaGroupHandler.kafkaGroupCreate(param);
        if (!result) {
            throw new RuntimeException("创建kafka group失败,retry");
        }
        return true;
    }


    @Override
    public List<KafkaBO.Group> kafkaGroupListQuery(String instanceName) {
        String groupListResponse = aliyunKafkaGroupHandler.kafkaGroupListQuery(instanceName);
        return Strings.isEmpty(groupListResponse) ? Collections.emptyList() : KafkaGroupConvert.toBOList(groupListResponse);
    }
}
