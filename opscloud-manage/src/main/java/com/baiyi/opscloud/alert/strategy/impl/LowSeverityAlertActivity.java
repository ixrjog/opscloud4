package com.baiyi.opscloud.alert.strategy.impl;

import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.alert.AlertNotifyMedia;
import com.baiyi.opscloud.alert.strategy.AlertSeverityEnum;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2022/7/21 12:05 AM
 * @Since 1.0
 */

@Component
public class LowSeverityAlertActivity extends AbstractAlertActivity {

    @Override
    public String getAlertSeverity() {
        return AlertSeverityEnum.LOW.name();
    }

    @Override
    public void executeAlertStrategy(AlertNotifyMedia media, AlertContext context) {

    }
}
