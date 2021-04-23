package com.baiyi.opscloud.facade.kafka.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.domain.vo.kafka.KafkaVO;
import com.baiyi.opscloud.facade.kafka.KafkaGroupFacade;
import com.baiyi.opscloud.kafka.bo.KafkaBO;
import com.baiyi.opscloud.kafka.center.KafkaGroupCenter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/18 11:23 上午
 * @Since 1.0
 */

@Component("KafkaGroupFacade")
public class KafkaGroupFacadeImpl implements KafkaGroupFacade {

    @Resource
    private KafkaGroupCenter kafkaGroupCenter;

    @Override
    public BusinessWrapper<KafkaVO.Group> kafkaGroupQuery(KafkaParam.GroupQuery param) {
        KafkaBO.Group group = kafkaGroupCenter.kafkaGroupQuery(param);
        if (group == null)
            return new BusinessWrapper<>(ErrorEnum.KAFKA_GROUP_NOT_EXIST);
        return new BusinessWrapper<>(BeanCopierUtils.copyProperties(group, KafkaVO.Group.class));
    }

    @Override
    public BusinessWrapper<Boolean> kafkaGroupCreate(KafkaParam.GroupCreate param) {
        KafkaParam.GroupQuery queryParam = new KafkaParam.GroupQuery();
        queryParam.setInstanceName(param.getInstanceName());
        queryParam.setConsumerId(param.getConsumerId());
        KafkaBO.Group group = kafkaGroupCenter.kafkaGroupQuery(queryParam);
        if (group != null)
            return new BusinessWrapper<>(ErrorEnum.KAFKA_GROUP_EXIST);
        if (kafkaGroupCenter.kafkaGroupCreate(param)) {
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.KAFKA_GROUP_CREATE_FAIL);
    }

    @Override
    public BusinessWrapper<Boolean> kafkaGroupCheck(String consumerId) {
        if (!RegexUtils.isKafkaTopic(consumerId))
            return new BusinessWrapper<>(ErrorEnum.KAFKA_GROUP_ERR);
        return BusinessWrapper.SUCCESS;
    }
}


