package com.baiyi.opscloud.alert.strategy.impl;

import com.baiyi.opscloud.alert.notify.NotifyFactory;
import com.baiyi.opscloud.alert.strategy.AlertStrategyFactory;
import com.baiyi.opscloud.alert.strategy.IAlertStrategy;
import com.baiyi.opscloud.domain.alert.AlertContext;
import com.baiyi.opscloud.domain.alert.AlertNotifyMedia;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyEvent;
import com.baiyi.opscloud.service.alert.AlertNotifyEventService;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author 修远
 * @Date 2022/7/20 11:21 PM
 * @Since 1.0
 */
public abstract class AbstractAlertActivity implements IAlertStrategy, InitializingBean {

    @Resource
    protected AlertNotifyEventService alertNotifyEventService;

    @Override
    public void afterPropertiesSet() throws Exception {
        AlertStrategyFactory.register(this);
    }

    @Override
    public void executeAlertStrategy(AlertNotifyMedia media, AlertContext context) {
        AlertNotifyEvent event = buildAlertNotifyEvent(context);
        alertNotifyEventService.add(event);
        getMediaList().forEach(mediaType -> NotifyFactory.getNotifyActivity(mediaType).doNotify(media, context));
    }

    protected abstract List<String> getMediaList();

    protected AlertNotifyEvent buildAlertNotifyEvent(AlertContext context) {
        return AlertNotifyEvent.builder()
                .eventUuid(context.getEventUuid())
                .alertName(context.getAlertName())
                .severity(context.getSeverity())
                .message(context.getMessage())
                .alertValue(context.getValue())
                .alertCheck(context.getCheck())
                .alertSource(context.getSource())
                .alertType(context.getAlertType())
                .service(context.getService())
                .metadata(JSONUtil.writeValueAsString(context.getMetadata()))
                .alertTime(new Date(context.getAlertTime()))
                .build();
    }
}
