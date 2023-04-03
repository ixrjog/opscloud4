package com.baiyi.opscloud.alert.notify.impl;

import com.baiyi.opscloud.alert.notify.INotify;
import com.baiyi.opscloud.alert.notify.NotifyFactory;
import com.baiyi.opscloud.domain.alert.AlertContext;
import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyEvent;
import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyHistory;
import com.baiyi.opscloud.service.alert.AlertNotifyEventService;
import com.baiyi.opscloud.service.alert.AlertNotifyHistoryService;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2022/7/21 12:14 AM
 * @Since 1.0
 */

public abstract class AbstractNotifyActivity implements INotify, InitializingBean {

    @Resource
    protected AlertNotifyHistoryService alertNotifyHistoryService;

    @Resource
    protected AlertNotifyEventService alertNotifyEventService;

    protected AlertNotifyHistory buildAlertNotifyHistory() {
        return AlertNotifyHistory.builder()
                .alertNotifyMedia(getKey())
                .build();
    }

    protected void saveAlertNotify(AlertContext context, List<AlertNotifyHistory> historyList) {
        AlertNotifyEvent event = alertNotifyEventService.getByUuid(context.getEventUuid());
        alertNotifyHistoryService.addList(
                historyList.stream()
                        .peek(history -> history.setAlertNotifyEventId(event.getId()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NotifyFactory.register(this);
    }

}
