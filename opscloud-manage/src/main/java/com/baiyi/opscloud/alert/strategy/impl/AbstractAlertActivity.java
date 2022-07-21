package com.baiyi.opscloud.alert.strategy.impl;

import com.baiyi.opscloud.alert.strategy.AlertStrategyFactory;
import com.baiyi.opscloud.alert.strategy.IAlertStrategy;
import org.springframework.beans.factory.InitializingBean;

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

}
