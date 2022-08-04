package com.baiyi.opscloud.alert.strategy.impl;

import com.baiyi.opscloud.alert.notify.NotifyFactory;
import com.baiyi.opscloud.alert.strategy.AlertStrategyFactory;
import com.baiyi.opscloud.alert.strategy.IAlertStrategy;
import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.alert.AlertNotifyMedia;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/7/20 11:21 PM
 * @Since 1.0
 */
public abstract class AbstractAlertActivity implements IAlertStrategy, InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {
        AlertStrategyFactory.register(this);
    }

    @Override
    public void executeAlertStrategy(AlertNotifyMedia media, AlertContext context) {
        getMediaList().forEach(mediaType -> NotifyFactory.getNotifyActivity(mediaType).doNotify(media, context));
    }

    protected abstract List<String> getMediaList();
}
