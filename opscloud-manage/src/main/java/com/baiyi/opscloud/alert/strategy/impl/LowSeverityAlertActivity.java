package com.baiyi.opscloud.alert.strategy.impl;

import com.baiyi.opscloud.alert.strategy.AlertSeverityEnum;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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
    protected List<String> getMediaList() {
        return Collections.emptyList();
    }
}
