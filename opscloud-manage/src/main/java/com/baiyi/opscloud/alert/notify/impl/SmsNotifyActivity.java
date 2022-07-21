package com.baiyi.opscloud.alert.notify.impl;

import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.alert.AlertNotifyMedia;
import com.baiyi.opscloud.alert.notify.NotifyMediaEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2022/7/21 12:19 AM
 * @Since 1.0
 */
@Slf4j
@Component
public class SmsNotifyActivity extends AbstractNotifyActivity {

    @Override
    public void doNotify(AlertNotifyMedia media, AlertContext context) {
        // todo 语音告警
        log.error("语音告警");
        // System.out.println("SmsNotifyActivity");
    }

    @Override
    public String getKey() {
        return NotifyMediaEnum.SMS.name();
    }
}
