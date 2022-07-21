package com.baiyi.opscloud.alert.strategy.impl;

import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.alert.AlertNotifyMedia;
import com.baiyi.opscloud.alert.notify.NotifyFactory;
import com.baiyi.opscloud.alert.notify.NotifyMediaEnum;
import com.baiyi.opscloud.alert.strategy.AlertSeverityEnum;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2022/7/21 12:02 AM
 * @Since 1.0
 */

@Component
public class MaxSeverityAlertActivity extends AbstractAlertActivity {

    @Override
    public String getAlertSeverity() {
        return AlertSeverityEnum.MAX.name();
    }

    @Override
    public void executeAlertStrategy(AlertNotifyMedia media, AlertContext context) {
        NotifyFactory.getNotifyActivity(NotifyMediaEnum.SMS.name()).doNotify(media, context);
    }
}
