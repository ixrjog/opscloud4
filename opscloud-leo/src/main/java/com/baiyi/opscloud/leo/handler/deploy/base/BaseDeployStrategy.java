package com.baiyi.opscloud.leo.handler.deploy.base;

import com.baiyi.opscloud.leo.handler.deploy.BaseDeployChainHandler;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2022/12/14 10:13
 * @Version 1.0
 */
public abstract class BaseDeployStrategy extends BaseDeployChainHandler implements IDeployStrategy, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        DeployStrategyFactory.register(this);
    }

}