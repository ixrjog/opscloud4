package com.baiyi.opscloud.alert.strategy.impl;

import com.baiyi.opscloud.alert.notify.NotifyMediaEnum;
import com.baiyi.opscloud.alert.strategy.AlertSeverityEnum;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/7/21 12:05 AM
 * @Since 1.0
 */

@Component
public class MidSeverityAlertActivity extends AbstractAlertActivity {

    @Override
    public String getAlertSeverity() {
        return AlertSeverityEnum.MID.name();
    }

    @Override
    protected List<String> getMediaList() {
        return Lists.newArrayList(
                NotifyMediaEnum.DINGTALK.name(),
                NotifyMediaEnum.SMS.name()
        );
    }
}
