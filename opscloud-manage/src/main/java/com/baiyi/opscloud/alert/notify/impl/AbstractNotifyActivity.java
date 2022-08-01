package com.baiyi.opscloud.alert.notify.impl;

import com.baiyi.opscloud.alert.notify.INotify;
import com.baiyi.opscloud.alert.notify.NotifyFactory;
import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyHistory;
import com.baiyi.opscloud.service.alert.AlertNotifyHistoryService;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author 修远
 * @Date 2022/7/21 12:14 AM
 * @Since 1.0
 */

public abstract class AbstractNotifyActivity implements INotify, InitializingBean {

    @Resource
    protected AlertNotifyHistoryService alertNotifyHistoryService;

    protected AlertNotifyHistory buildAlertNotifyHistory(AlertContext context) {
        return AlertNotifyHistory.builder()
                .alertName(context.getAlertName())
                .severity(context.getSeverity())
                .message(context.getMessage())
                .alertValue(context.getValue())
                .alertCheck(context.getCheck())
                .alertSource(context.getSource())
                .alertType(context.getAlertType())
                .service(context.getService())
                .alertNotifyMedia(getKey())
                .metadata(JSONUtil.writeValueAsString(context.getMetadata()))
                .alertTime(new Date(context.getAlertTime()))
                .build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NotifyFactory.register(this);
    }

}
