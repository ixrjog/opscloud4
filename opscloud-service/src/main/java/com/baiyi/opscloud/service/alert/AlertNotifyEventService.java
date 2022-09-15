package com.baiyi.opscloud.service.alert;

import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyEvent;

/**
 * @Author 修远
 * @Date 2022/9/14 10:32 AM
 * @Since 1.0
 */
public interface AlertNotifyEventService {

    void add(AlertNotifyEvent alertNotifyEvent);

    AlertNotifyEvent getByUuid(String eventUuid);
}
