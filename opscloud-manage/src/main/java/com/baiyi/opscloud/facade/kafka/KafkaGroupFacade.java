package com.baiyi.opscloud.facade.kafka;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.domain.vo.kafka.KafkaVO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/18 11:22 上午
 * @Since 1.0
 */
public interface KafkaGroupFacade {

    BusinessWrapper<KafkaVO.Group> kafkaGroupQuery(KafkaParam.GroupQuery param);

    BusinessWrapper<Boolean> kafkaGroupCreate(KafkaParam.GroupCreate param);

    BusinessWrapper<Boolean> kafkaGroupCheck(String consumerId);
}
