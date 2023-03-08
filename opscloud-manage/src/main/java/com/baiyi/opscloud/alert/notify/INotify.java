package com.baiyi.opscloud.alert.notify;

import com.baiyi.opscloud.domain.alert.AlertContext;
import com.baiyi.opscloud.domain.alert.AlertNotifyMedia;

/**
 * @Author 修远
 * @Date 2022/7/21 12:06 AM
 * @Since 1.0
 */
public interface INotify {

    void doNotify(AlertNotifyMedia media, AlertContext context);

    String getKey();

}
