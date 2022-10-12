package com.baiyi.opscloud.alert.strategy;

import com.baiyi.opscloud.domain.alert.AlertContext;
import com.baiyi.opscloud.domain.alert.AlertNotifyMedia;

/**
 * @Author 修远
 * @Date 2022/7/20 11:20 PM
 * @Since 1.0
 */
public interface IAlertStrategy {

    String getAlertSeverity();

    void executeAlertStrategy(AlertNotifyMedia media, AlertContext context);
}
