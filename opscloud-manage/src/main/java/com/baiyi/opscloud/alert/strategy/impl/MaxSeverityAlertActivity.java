package com.baiyi.opscloud.alert.strategy.impl;

import com.baiyi.opscloud.alert.notify.NotifyMediaEnum;
import com.baiyi.opscloud.alert.strategy.AlertSeverityEnum;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

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
    protected List<String> getMediaList() {
        return Lists.newArrayList(
                NotifyMediaEnum.VMS.name(),
                NotifyMediaEnum.DINGTALK.name(),
                NotifyMediaEnum.SMS.name()
        );
    }
}
