package com.baiyi.opscloud.alert.notify.impl;

import com.baiyi.opscloud.alert.notify.INotify;
import com.baiyi.opscloud.alert.notify.NotifyFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author 修远
 * @Date 2022/7/21 12:14 AM
 * @Since 1.0
 */

public abstract class AbstractNotifyActivity implements INotify, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        NotifyFactory.register(this);
    }

}
