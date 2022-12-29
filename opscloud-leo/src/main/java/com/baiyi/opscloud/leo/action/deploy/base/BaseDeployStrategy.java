package com.baiyi.opscloud.leo.action.deploy.base;

import com.baiyi.opscloud.leo.action.deploy.BaseDeployHandler;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2022/12/14 10:13
 * @Version 1.0
 */
public abstract class BaseDeployStrategy extends BaseDeployHandler implements IDeployStrategy, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        DeployStrategyFactory.register(this);
    }

}
