package com.baiyi.opscloud.service.alert.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyEvent;
import com.baiyi.opscloud.mapper.opscloud.AlertNotifyEventMapper;
import com.baiyi.opscloud.service.alert.AlertNotifyEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author 修远
 * @Date 2022/9/14 10:33 AM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class AlertNotifyEventServiceImpl implements AlertNotifyEventService {

    private final AlertNotifyEventMapper alertNotifyEventMapper;

    @Override
    public void add(AlertNotifyEvent alertNotifyEvent) {
        alertNotifyEventMapper.insert(alertNotifyEvent);
    }

    @Override
    public AlertNotifyEvent getByUuid(String eventUuid) {
        Example example = new Example(AlertNotifyEvent.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eventUuid", eventUuid);
        return alertNotifyEventMapper.selectOneByExample(example);
    }
}
